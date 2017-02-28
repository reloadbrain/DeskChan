import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import org.apache.commons.lang3.SystemUtils

final class Localization {
	
	static final userCountry = System.getProperty("user.country")
	static final userLanguage = System.getProperty("user.language")
	static final Properties strings = new Properties()
	
	static String getString(String key) {
		try {
			String s = strings.getProperty(key, key)
			return new String(s.getBytes("ISO-8859-1"), "UTF-8")
		} catch (Throwable e) {
			return key
		}
	}
	
	static void load() {
		String codeLocation = Localization.class.protectionDomain.codeSource.location.path
		if (SystemUtils.IS_OS_WINDOWS) {
			if (codeLocation.size() >= 3) {
				if ((codeLocation.charAt(0) == '/') && (codeLocation.charAt(2) == ':')) {
					codeLocation = codeLocation.substring(1);
				}
			}
		}
		Path resourcesPath = Paths.get(codeLocation)
				.getParent().resolve("resources");
		Path stringsPath = resourcesPath.resolve("strings_" + userLanguage + "_" + userCountry + ".properties")
		if (!Files.isReadable(stringsPath)) {
			stringsPath = resourcesPath.resolve("strings_" + userLanguage + ".properties")
			if (!Files.isReadable(stringsPath)) {
				stringsPath = resourcesPath.resolve("strings.properties")
			}
		}
		if (Files.isReadable(stringsPath)) {
			try {
				strings.load(Files.newInputStream(stringsPath))
			} catch (Exception e) {
				e.printStackTrace()
			}
		}
	}
	
}
