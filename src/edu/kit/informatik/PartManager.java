package edu.kit.informatik;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.stream.Collectors;

class PartManager {

    /**
     * Main instance of PartManager
     */
    static final PartManager MAIN = new PartManager();
    private static final String EMPTY_STRING = "EMPTY";
    private PartList list = new PartList();

    private Result<Part> getAssemblyWith(String id) {
        if (!list.contains(id))
            return new Result<>(null, new Error(Error.Type.PART_DOESNT_EXIST, id));

        Part part = list.getPartWith(id);
        if (part.getType() == Part.Type.COMPONENT)
            return new Result<>(null, new Error(Error.Type.PART_IS_COMPONENT, id));

        return new Result<>(part, null);
    }

    /**
     * Adds a new Assembly Part with the given ID and children,
     * if not already present
     *
     * @param id Unique ID of the new Part
     * @param children IDs of the children Parts
     * @return Result without a value
     */
    Result<Void> addAssemblyWith(String id, HashMap<String, Integer> children) {
        Part existingPart = list.getPartWith(id);
        if (existingPart != null && existingPart.getType() == Part.Type.ASSEMBLY) {
            return new Result<>(null, new Error(Error.Type.ASSEMBLY_ALREADY_EXISTS, id));
        }

        PartList newList = new PartList(list);

        if (existingPart == null) {
            newList.addPart(new Part(id, children));
        } else {
            newList.getPartWith(id).addChildren(children);
        }

        newList.addAllIfNotPresent(children.keySet());

        String cycleRoot = newList.cycleRoot();
        if (cycleRoot != null) {
            return new Result<>(null, new Error(Error.Type.NOT_ACYCLIC, cycleRoot));
        }

        list = newList;
        return new Result<>(null, null);

    }

    /**
     * Removes the assembly with given ID if present
     *
     * @param id ID of the part to remove
     * @return Result without value
     */
    Result<Void> removeAssemblyWith(String id) {
        Result<Part> partResult = getAssemblyWith(id);
        if (!partResult.isSuccessful())
            return new Result<>(null, partResult.error);
        Part part = partResult.value;

        if (list.partHasParents(part)) {
            part.removeAllChildren();
        } else {
            list.removePart(part);
        }

        list.postRemovalCleanup();

        return new Result<>(null, null);
    }

    /**
     * Builds a string with names and amounts of the part with given IDs children
     *
     * @param id ID of the part to print
     * @return Result with string value
     */
    Result<String> printAssemblyWith(String id) {
        if (!list.contains(id))
            return new Result<>(null, new Error(Error.Type.PART_DOESNT_EXIST, id));

        Part part = list.getPartWith(id);
        if (part.getType() == Part.Type.COMPONENT)
            return new Result<>(Part.Type.COMPONENT.toString(), null);

        HashMap<String, Integer> children = part.getChildren();
        List<String> childrenSorted = children.keySet().stream().sorted().collect(Collectors.toList());

        StringBuilder out = new StringBuilder();
        for (int i = 0; i < childrenSorted.size(); i++) {
            String child = childrenSorted.get(i);
            Integer amount = children.get(child);

            out.append(String.format("%s%s%d", child, BlueprintZ.Defaults.NAME_AMOUNT_SEPARATOR, amount));

            if (i != childrenSorted.size() - 1) {
                out.append(BlueprintZ.Defaults.PART_SEPARATOR);
            }
        }

        return new Result<>(out.toString(), null);
    }

    private String sortAndBuildString(HashMap<Part, Integer> parts) {
        List<Part> sortedParts = parts.entrySet().stream().sorted((o1, o2) -> {
            if (!o1.getValue().equals(o2.getValue()))
                return o2.getValue().compareTo(o1.getValue());
            else
                return o1.getKey().getId().compareTo(o2.getKey().getId());
        }).map(Map.Entry::getKey
        ).collect(Collectors.toList());

        StringBuilder out = new StringBuilder();
        for (int i = 0; i < sortedParts.size(); i++) {
            Part assembly = sortedParts.get(i);
            out.append(String.format("%s%s%d",
                    assembly.getId(),
                    BlueprintZ.Defaults.NAME_AMOUNT_SEPARATOR,
                    parts.get(assembly)));

            if (i != sortedParts.size() - 1)
                out.append(BlueprintZ.Defaults.PART_SEPARATOR);
        }

        return out.toString();
    }

    /**
     * Gives every direct and indirect child-assembly of the part
     * with given ID
     *
     * @param id ID of part whose children will be inspected
     * @return Result with String
     */
    Result<String> getAssembliesOf(String id) {
        Result<Part> partResult = getAssemblyWith(id);
        if (!partResult.isSuccessful())
            return new Result<>(null, partResult.error);
        Part part = partResult.value;

        HashMap<Part, Integer> assemblies = list.childrenOf(part, Part.Type.ASSEMBLY);
        if (assemblies.isEmpty())
            return new Result<>(EMPTY_STRING, null);

        String out = sortAndBuildString(assemblies);

        return new Result<>(out, null);
    }

    /**
     * Gives every direct and indirect child-component of the part
     * with given ID
     *
     * @param id ID of part whose children will be inspected
     * @return Result with String
     */
    Result<String> getComponentsOf(String id) {
        Result<Part> partResult = getAssemblyWith(id);
        if (!partResult.isSuccessful())
            return new Result<>(null, partResult.error);
        Part part = partResult.value;

        HashMap<Part, Integer> components = list.childrenOf(part, Part.Type.COMPONENT);
        if (components.isEmpty())
            return new Result<>(EMPTY_STRING, null);

        String out = sortAndBuildString(components);

        return new Result<>(out, null);
    }

    /**
     * Adds the part with given ID to the part with given ID.
     * If the source part does not exist, creates it first.
     *
     * @param toId ID of the target part
     * @param id ID of the source part
     * @param amount Amount of source part in target part
     * @return Result without value
     */
    Result<Void> addPart(String toId, String id, Integer amount) {
        PartList newList = new PartList(list);
        if (!newList.contains(toId))
            return new Result<>(null, new Error(Error.Type.PART_DOESNT_EXIST, toId));
        Part targetPart = newList.getPartWith(toId);
        if (targetPart.getType() == Part.Type.COMPONENT)
            return new Result<>(null, new Error(Error.Type.PART_IS_COMPONENT, toId));

        newList.addIfNotPresent(id);
        targetPart.addChild(id, amount);
        String cycleRoot = newList.cycleRoot();
        if (cycleRoot != null) {
            return new Result<>(null, new Error(Error.Type.NOT_ACYCLIC, cycleRoot));
        }

        list = newList;
        return new Result<>(null, null);
    }

    /**
     * Removes given part from source assembly if there
     * is enough of it
     *
     * @param fromId ID of source assembly
     * @param id ID of part to remove
     * @param amount Amount of parts to remove
     * @return Result without value
     */
    Result<Void> removePart(String fromId, String id, Integer amount) {
        Result<Part> partResult = getAssemblyWith(fromId);
        if (!partResult.isSuccessful())
            return new Result<>(null, partResult.error);
        Part part = partResult.value;

        if (part.removeChild(id, amount)) {
            list.postRemovalCleanup();
            return new Result<>(null, null);
        } else {
            return new Result<>(null, new Error(Error.Type.NO_ENOUGH_PARTS, amount.toString()));
        }
    }
}