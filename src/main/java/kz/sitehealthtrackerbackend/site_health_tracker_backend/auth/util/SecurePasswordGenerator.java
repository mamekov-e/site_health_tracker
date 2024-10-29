package kz.sitehealthtrackerbackend.site_health_tracker_backend.auth.util;

import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;

import java.util.Arrays;

public class SecurePasswordGenerator {
    private SecurePasswordGenerator() {
    }

    public static String generatePassword() {
        PasswordGenerator gen = new PasswordGenerator();

        CharacterRule upperCaseRule = new CharacterRule(EnglishCharacterData.UpperCase);
        CharacterRule lowerCaseRule = new CharacterRule(EnglishCharacterData.LowerCase);
        CharacterRule digitRule = new CharacterRule(EnglishCharacterData.Digit);
        CharacterRule specialRule = new CharacterRule(EnglishCharacterData.Alphabetical);

        return gen.generatePassword(16, Arrays.asList(
                upperCaseRule, lowerCaseRule, digitRule, specialRule));
    }
}
