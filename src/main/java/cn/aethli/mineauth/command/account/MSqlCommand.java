package cn.aethli.mineauth.command.account;

import cn.aethli.mineauth.command.BaseCommand;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;

import java.util.ArrayList;
import java.util.List;

/** @author 93162 */
public class MSqlCommand extends BaseCommand {
    public static final String COMMAND = "resetPassword";
    private static final List<String> PARAMETERS = new ArrayList<>();

    static {
        PARAMETERS.add("userName");
    }


    public MSqlCommand() {
        super(COMMAND, PARAMETERS, 4);
    }

    @Override
    public int run(CommandContext<CommandSource> context) throws CommandSyntaxException {
        return 0;
    }
}

