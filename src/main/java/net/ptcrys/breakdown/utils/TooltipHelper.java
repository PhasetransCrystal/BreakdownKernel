package net.ptcrys.breakdown.utils;

import net.ptcrys.breakdown.BreakdownKernal;
import net.ptcrys.breakdown.api.BreaApi;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.UnaryOperator;

public class TooltipHelper {

    public static final UnaryOperator<Style> RAINBOW_HSL = (style) -> style.withColor(rainbowColor(2.5F));
    public static final UnaryOperator<Style> RAINBOW_HSL_SLOW = (style) -> style.withColor(rainbowColor(1.25F));
    public static final UnaryOperator<Style> RAINBOW_HSL_FAST = (style) -> style.withColor(rainbowColor(3.75F));
    private static final List<BreaFormattingCode> CODES = new ArrayList<>();
    private static final ChatFormatting[] ALL_COLORS;
    public static final BreaFormattingCode RAINBOW_FAST;
    public static final BreaFormattingCode RAINBOW;
    public static final BreaFormattingCode RAINBOW_SLOW;
    public static final BreaFormattingCode BLINKING_CYAN;
    public static final BreaFormattingCode BLINKING_RED;
    public static final BreaFormattingCode BLINKING_ORANGE;
    public static final BreaFormattingCode BLINKING_GRAY;

    public static TextColor rainbowColor(float speed) {
        return TextColor.fromRgb(ColorUtil.toRGB((float) (BreaApi.CLIENT_TIME & 1048575) * speed, 95.0F, 60.0F));
    }

    public static BreaFormattingCode createNewCode(int rate, ChatFormatting... codes) {
        if (rate <= 0) {
            BreakdownKernal.LOGGER.error("Could not create GT Formatting Code with rate {}, must be greater than zero!", rate);
            return null;
        } else if (codes != null && codes.length > 1) {
            BreaFormattingCode code = new BreaFormattingCode(rate, codes);
            CODES.add(code);
            return code;
        } else {
            BreakdownKernal.LOGGER.error("Could not create GT Formatting Code with codes {}, must have length greater than one!", Arrays.toString(codes));
            return null;
        }
    }

    public static void onClientTick() {
        CODES.forEach(BreaFormattingCode::updateIndex);
    }

    static {
        ALL_COLORS = new ChatFormatting[] { ChatFormatting.RED, ChatFormatting.GOLD, ChatFormatting.YELLOW, ChatFormatting.GREEN, ChatFormatting.AQUA, ChatFormatting.DARK_AQUA, ChatFormatting.DARK_BLUE, ChatFormatting.BLUE, ChatFormatting.DARK_PURPLE, ChatFormatting.LIGHT_PURPLE };
        RAINBOW_FAST = createNewCode(1, ALL_COLORS);
        RAINBOW = createNewCode(5, ALL_COLORS);
        RAINBOW_SLOW = createNewCode(25, ALL_COLORS);
        BLINKING_CYAN = createNewCode(5, ChatFormatting.AQUA, ChatFormatting.WHITE);
        BLINKING_RED = createNewCode(5, ChatFormatting.RED, ChatFormatting.WHITE);
        BLINKING_ORANGE = createNewCode(25, ChatFormatting.GOLD, ChatFormatting.YELLOW);
        BLINKING_GRAY = createNewCode(25, ChatFormatting.GRAY, ChatFormatting.DARK_GRAY);
    }

    public static class BreaFormattingCode {

        private final int rate;
        private final ChatFormatting[] codes;
        private int index = 0;

        private BreaFormattingCode(int rate, ChatFormatting... codes) {
            this.rate = rate;
            this.codes = codes;
        }

        public void updateIndex() {
            if (BreaApi.CLIENT_TIME % this.rate == 0) {
                if (this.index + 1 >= this.codes.length) {
                    this.index = 0;
                } else {
                    ++this.index;
                }
            }
        }

        public ChatFormatting getCurrent() {
            return this.codes[this.index];
        }

        public String toString() {
            return this.codes[this.index].toString();
        }
    }
}
