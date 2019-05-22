package org.ethan.peer.injection.injectors;


import org.ethan.peer.callbacks.HttpRequests;
import org.ethan.peer.injection.Injector;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;


import java.util.ListIterator;

public class PostRequest implements Injector {

    @Override
    public boolean condition(ClassNode classNode) {
        return "com/mashape/unirest/http/Unirest".equals(classNode.name);
    }

    @Override
    public void inject(ClassNode classNode) {
        @SuppressWarnings("unchecked")
        ListIterator<MethodNode> mnIt = classNode.methods.listIterator();
        while (mnIt.hasNext()) {
            MethodNode mn = mnIt.next();
            if (mn.name.equals("post")) {
                injectPost(mn);
            } else if(mn.name.equals("get")) {
                injectGet(mn);
            }
        }
    }

    @SuppressWarnings("deprecation")
    private void injectPost(MethodNode mn) {
            InsnList nl = new InsnList();
            boolean added = false;
            AbstractInsnNode[] mnNodes = mn.instructions.toArray();
            for (AbstractInsnNode abstractInsnNode : mnNodes) {
                if(!added) {
                    nl.add(new VarInsnNode(Opcodes.ALOAD, 0));
                    nl.add(new MethodInsnNode(Opcodes.INVOKESTATIC, HttpRequests.class.getCanonicalName().replace('.', '/'), "printPost", "(" + "Ljava/lang/String;" + ")V"));
                    System.out.println("Injecting inside post request.");
                    added = true;
                }
                nl.add(abstractInsnNode);
            }

            mn.instructions = nl;
            mn.visitMaxs(0, 0);
            mn.visitEnd();
        }
    @SuppressWarnings("deprecation")
    private void injectGet(MethodNode mn) {
        InsnList nl = new InsnList();
        boolean added = false;
        AbstractInsnNode[] mnNodes = mn.instructions.toArray();
        for (AbstractInsnNode abstractInsnNode : mnNodes) {
            if(!added) {
                nl.add(new VarInsnNode(Opcodes.ALOAD, 0));
                nl.add(new MethodInsnNode(Opcodes.INVOKESTATIC, HttpRequests.class.getCanonicalName().replace('.', '/'), "printGet", "(" + "Ljava/lang/String;" + ")V"));
                System.out.println("Injecting inside get request.");
                added = true;
            }
            nl.add(abstractInsnNode);
        }

        mn.instructions = nl;
        mn.visitMaxs(0, 0);
        mn.visitEnd();
    }
}
