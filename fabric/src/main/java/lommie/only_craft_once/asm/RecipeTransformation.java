package lommie.only_craft_once.asm;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.Objects;

public class RecipeTransformation {
    public static void transformClass(ClassNode node){
        MethodNode method = node.methods.stream().filter(m -> Objects.equals(m.name, "matches")).findFirst().orElseThrow();

        InsnList newins = new InsnList();
        newins.add(new LdcInsnNode("it works!"));
        newins.add(new FieldInsnNode(
                Opcodes.GETSTATIC,
                "java/lang/System",
                "out",
                "Ljava/io/PrintStream;"
        ));
        newins.add(new InsnNode(Opcodes.SWAP));
        newins.add(new MethodInsnNode(
                Opcodes.INVOKEVIRTUAL,
                "java/io/PrintStream",
                "println",
                "(Ljava/lang/String;)V",
                false
        ));
        method.instructions.insert(newins);
    }
}
