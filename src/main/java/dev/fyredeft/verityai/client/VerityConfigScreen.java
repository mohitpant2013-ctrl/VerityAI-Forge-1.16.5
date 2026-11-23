package dev.fyredeft.verityai.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.fyredeft.verityai.VerityAI;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.StringTextComponent;

/** In-game Mods -> Verity AI -> Config screen. */
public class VerityConfigScreen extends Screen {
    private final Screen parent;
    private TextFieldWidget apiKeyField;
    private TextFieldWidget modelField;
    private String status = "Your key is stored locally in this Minecraft instance.";

    public VerityConfigScreen(Screen parent) {
        super(new StringTextComponent("Verity AI Settings"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int center = this.width / 2;
        this.apiKeyField = new TextFieldWidget(this.font, center - 145, 65, 290, 20,
                new StringTextComponent("Gemini API key"));
        this.apiKeyField.setMaxLength(512);
        this.apiKeyField.setValue(VerityAI.API_KEY.get());
        this.addButton(this.apiKeyField);

        this.modelField = new TextFieldWidget(this.font, center - 145, 112, 290, 20,
                new StringTextComponent("Gemini model"));
        this.modelField.setMaxLength(100);
        this.modelField.setValue(VerityAI.MODEL.get());
        this.addButton(this.modelField);

        this.addButton(new Button(center - 145, 155, 140, 20,
                new StringTextComponent("Save settings"), button -> saveSettings()));
        this.addButton(new Button(center + 5, 155, 140, 20,
                new StringTextComponent("Done"), button -> onClose()));
        this.apiKeyField.setFocus(true);
    }

    private void saveSettings() {
        String key = this.apiKeyField.getValue().trim();
        String model = this.modelField.getValue().trim();
        if (key.isEmpty()) {
            this.status = "Enter your Gemini API key before saving.";
            return;
        }
        if (model.isEmpty()) model = "gemini-2.5-flash";

        VerityAI.API_KEY.set(key);
        VerityAI.MODEL.set(model);
        VerityAI.API_KEY.save();
        VerityAI.MODEL.save();
        this.status = "Saved. Use /verity ask <message> in your world.";
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(this.parent);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        drawCenteredString(matrixStack, this.font, this.title, this.width / 2, 28, 0xFFFFFF);
        this.font.draw(matrixStack, "Gemini API key", this.width / 2 - 145, 51, 0xC8C8C8);
        this.font.draw(matrixStack, "Model (default: gemini-2.5-flash)",
                this.width / 2 - 145, 98, 0xC8C8C8);
        this.font.draw(matrixStack, this.status, this.width / 2 - 145, 190, 0xA0D8A0);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }
}
