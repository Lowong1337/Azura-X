package fr.ducouscous.mcscript.mc;

import fr.ducouscous.csl.running.variable.Variable;
import fr.ducouscous.csl.running.variable.values.Boolean;
import fr.ducouscous.csl.running.variable.values.Number;
import fr.ducouscous.csl.running.variable.values.StringVar;
import fr.ducouscous.csl.running.variable.values.Table;
import net.minecraft.client.settings.KeyBinding;

public class KeybindTable extends Table {
    private final KeyBinding binding;
    public KeybindTable(KeyBinding binding) {
        this.binding = binding;
    }

    @Override
    public Variable getValue(Object index) {
        switch (index.toString()) {
            case "pressed":
                return new Variable(new Boolean(binding.pressed));
            case "pressTime":
                return new Variable(new Number(binding.pressTime));
            case "keyCode":
                return new Variable(new Number(binding.keyCode));
        }

        return super.getValue(index);
    }

    @Override
    public void setValue(Object index, Variable value) {
        switch (index.toString()) {
            case "pressed":
                binding.pressed = (boolean) value.getValue().value();
                break;
            case "pressTime":
                binding.pressTime = (int) value.getValue().value();
                break;
            case "keyCode":
                binding.keyCode = (int) value.getValue().value();
                break;
            default:
                super.setValue(index, value);
                break;
        }
    }
}
