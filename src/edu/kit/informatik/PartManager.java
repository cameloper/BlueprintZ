package edu.kit.informatik;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

class PartManager {
    /**
     * Main instance of PartManager
     */
    static PartManager main = new PartManager();
    private PartList list = new PartList();

    /**
     * Adds a new Assembly Part with the given ID and children,
     * if not already present
     *
     * @param id Unique ID of the new Part
     * @param children IDs of the children Parts
     * @return Result without a value
     */
    Result<Void> addAssemblyWith(String id, HashMap<String, Integer> children) {
        if (children.values().stream().anyMatch(v -> v < 1)) {
            return new Result<>(null, new Error(Error.Type.NUMBER_NOT_IN_RANGE));
        }

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

        newList.addIfNotPresent(children.keySet());

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
        if (!list.hasPartWith(id))
            return new Result<>(null, new Error(Error.Type.PART_DOESNT_EXIST, id));

        Part part = list.getPartWith(id);
        if (part.getType() == Part.Type.COMPONENT)
            return new Result<>(null, new Error(Error.Type.PART_IS_COMPONENT, id));

        if (list.partHasParents(id)) {
            part.removeAllChildren();
        } else {
            list.removePart(part);
        }

        return new Result<>(null, null);
    }

    /**
     * Builds a string with names and amounts of the part with given IDs children
     *
     * @param id ID of the part to print
     * @return Result with string value
     */
    Result<String> printAssemblyWith(String id) {
        if (!list.hasPartWith(id))
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

            out.append(String.format("%s:%d", child, amount));

            if (i != childrenSorted.size() - 1) {
                out.append(";");
            }
        }

        return new Result<>(out.toString(), null);
    }
}