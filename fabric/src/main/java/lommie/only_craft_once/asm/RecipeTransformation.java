package lommie.only_craft_once.asm;

import lommie.only_craft_once.Constants;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.Objects;

public class RecipeTransformation {
    public static void transformClass(ClassNode node){
        MethodNode method = node.methods.stream().filter(m -> Objects.equals(m.name, "matches")).findFirst().orElseThrow();
        Constants.LOG.error(method.toString());

        InsnList newins = new InsnList();

        newins.add(new FieldInsnNode(
                Opcodes.GETSTATIC,
                "java/lang/System",
                "out",
                "Ljava/io/PrintStream;"
        ));
        newins.add(new LdcInsnNode("it works!"));
        newins.add(new MethodInsnNode(
                Opcodes.INVOKEVIRTUAL,
                "java/io/PrintStream",
                "println",
                "(Ljava/lang/String;)V",
                false
        ));



        newins.add(new VarInsnNode(Opcodes.ALOAD, 2));
        newins.add(new MethodInsnNode(
                Opcodes.INVOKEVIRTUAL,
                "java/lang/Object",
                "toString",
                "()Ljava/lang/String;",
                false
        ));
        newins.add(new MethodInsnNode(
                Opcodes.INVOKEVIRTUAL,
                "java/io/PrintStream",
                "println",
                "(Ljava/lang/String;)V",
                false
        ));

        method.instructions.insert(newins);

        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        method.accept(writer);
    }
}
