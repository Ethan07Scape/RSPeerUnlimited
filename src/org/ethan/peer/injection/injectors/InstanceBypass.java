package org.ethan.peer.injection.injectors;


import org.ethan.peer.injection.Injector;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.List;
import java.util.ListIterator;

public class InstanceBypass implements Injector {

    @Override
    public boolean condition(ClassNode classNode) {
        if (classNode.fields.size() == 2 && classNode.access == 33) {
            if (classNode.methods.size() == 4) {
                if (countFieldType("I", classNode) == 1 && countFieldType("Z", classNode) == 1) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void inject(ClassNode classNode) {
        @SuppressWarnings("unchecked")
        ListIterator<MethodNode> mnIt = classNode.methods.listIterator();
        while (mnIt.hasNext()) {
            MethodNode mn = mnIt.next();
            if (mn.desc.equals("()Z")) {
                inject(mn, classNode);
            }
        }
    }

    public String getSetter(ClassNode c) {
        List<MethodNode> nodes = c.methods;
        for (MethodNode m : nodes) {
            if (m.desc.equals("()V") && !m.name.equals("<clinit>")) {
                if (m.instructions.size() < 50) {
                    return m.name;
                }
            }
        }
        return "null";
    }

    @SuppressWarnings("deprecation")
    private void inject(MethodNode mn, ClassNode node) {
        InsnList nl = new InsnList();
        boolean added = false;
        AbstractInsnNode[] mnNodes = mn.instructions.toArray();
        for (AbstractInsnNode abstractInsnNode : mnNodes) {
            if (!added) {
                nl.add(new MethodInsnNode(Opcodes.INVOKESTATIC, node.name, getSetter(node), "()V"));
                System.out.println("Injecting load instance bypass");
                added = true;
            }
            nl.add(abstractInsnNode);
        }

        mn.instructions = nl;
        mn.visitMaxs(0, 0);
        mn.visitEnd();
    }

    private int countFieldType(String type, ClassNode node) {
        int i = 0;
        List<FieldNode> nodes = node.fields;
        for (FieldNode f : nodes) {
            if (f.desc.equals(type)) {
                i++;
            }
        }
        return i;
    }
}
