package com.studerw.support.web;

import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static com.studerw.support.web.FlashMessage.MESSAGE_ATTRIBUTE;

public final class FlashMessageHelper {

    private FlashMessageHelper() {

    }

    public static void addSuccessAttribute(RedirectAttributes ra, String message, Object... args) {
        addAttribute(ra, message, FlashMessage.Type.SUCCESS, args);
    }

    public static void addErrorAttribute(RedirectAttributes ra, String message, Object... args) {
        addAttribute(ra, message, FlashMessage.Type.DANGER, args);
    }

    public static void addInfoAttribute(RedirectAttributes ra, String message, Object... args) {
        addAttribute(ra, message, FlashMessage.Type.INFO, args);
    }

    public static void addWarningAttribute(RedirectAttributes ra, String message, Object... args) {
        addAttribute(ra, message, FlashMessage.Type.WARNING, args);
    }

    private static void addAttribute(RedirectAttributes ra, String message, FlashMessage.Type type, Object... args) {
        ra.addFlashAttribute(MESSAGE_ATTRIBUTE, new FlashMessage(message, type, args));
    }

    public static void addSuccessAttribute(Model model, String message, Object... args) {
        addAttribute(model, message, FlashMessage.Type.SUCCESS, args);
    }

    public static void addErrorAttribute(Model model, String message, Object... args) {
        addAttribute(model, message, FlashMessage.Type.DANGER, args);
    }

    public static void addInfoAttribute(Model model, String message, Object... args) {
        addAttribute(model, message, FlashMessage.Type.INFO, args);
    }

    public static void addWarningAttribute(Model model, String message, Object... args) {
        addAttribute(model, message, FlashMessage.Type.WARNING, args);
    }

    private static void addAttribute(Model model, String message, FlashMessage.Type type, Object... args) {
        model.addAttribute(MESSAGE_ATTRIBUTE, new FlashMessage(message, type, args));
    }
}
