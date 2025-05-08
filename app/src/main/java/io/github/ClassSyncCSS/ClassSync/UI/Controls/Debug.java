package io.github.ClassSyncCSS.ClassSync.UI.Controls;

import imgui.ImGui;

public class Debug {

    public void process() {
        ImGui.begin("Debug");
            ImGui.text(String.format("FPS: %f", ImGui.getIO().getFramerate()));
        ImGui.end();
    }
}
