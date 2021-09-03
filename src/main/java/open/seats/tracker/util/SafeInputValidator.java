package open.seats.tracker.util;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang.StringUtils;
import org.owasp.esapi.ESAPI;
import org.springframework.stereotype.Component;

@Component(value = "safeInputValidator")
public class SafeInputValidator implements ConstraintValidator<SafeInput, String> {

	@Override
	public void initialize(SafeInput constraintAnnotation) {
		// no initialization
	}

	@Override
	public boolean isValid(String input, ConstraintValidatorContext context) {

		String safeText = null;

		if (StringUtils.isNotBlank(input)) {
			// ESAPI library to avoid encoded attacks.
			safeText = ESAPI.encoder().canonicalize(input);

			if(safeText.startsWith("=")) return false;
			
			// Avoid null characters
			safeText = safeText.replaceAll("", "");

			// Avoid anything between script tags
			Pattern scriptPattern = Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE);
			safeText = scriptPattern.matcher(safeText).replaceAll("");

			// Avoid anything in a src='...' type of expression
			scriptPattern = Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'",
					Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
			safeText = scriptPattern.matcher(safeText).replaceAll("");

			scriptPattern = Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"",
					Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
			safeText = scriptPattern.matcher(safeText).replaceAll("");

			// Remove any lonesome </script> tag
			scriptPattern = Pattern.compile("</script>", Pattern.CASE_INSENSITIVE);
			safeText = scriptPattern.matcher(safeText).replaceAll("");

			// Remove any lonesome <script ...> tag
			scriptPattern = Pattern.compile("<script(.*?)>",
					Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
			safeText = scriptPattern.matcher(safeText).replaceAll("");

			// Avoid eval(...) expressions
			scriptPattern = Pattern.compile("eval\\((.*?)\\)",
					Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
			safeText = scriptPattern.matcher(safeText).replaceAll("");

			// Avoid expression(...) expressions
			scriptPattern = Pattern.compile("expression\\((.*?)\\)",
					Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
			safeText = scriptPattern.matcher(safeText).replaceAll("");

			// Avoid javascript:... expressions
			scriptPattern = Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE);
			safeText = scriptPattern.matcher(safeText).replaceAll("");

			// Avoid vbscript:... expressions
			scriptPattern = Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE);
			safeText = scriptPattern.matcher(safeText).replaceAll("");

			// Avoid onload= expressions
			scriptPattern = Pattern.compile("onload(.*?)=",
					Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
			safeText = scriptPattern.matcher(safeText).replaceAll("");

			// Avoid onmouseover = expressions
			scriptPattern = Pattern.compile("onmouseover(.*?)=",
					Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
			safeText = scriptPattern.matcher(safeText).replaceAll("");

			// Avoid onerror = expressions
			scriptPattern = Pattern.compile("onerror(.*?)=",
					Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
			safeText = scriptPattern.matcher(safeText).replaceAll("");

			// Avoid Anchor tags
			scriptPattern = Pattern.compile("<a>(.*?)</a>",
					Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
			safeText = scriptPattern.matcher(safeText).replaceAll("");

			scriptPattern = Pattern.compile("<a(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
			safeText = scriptPattern.matcher(safeText).replaceAll("");

			scriptPattern = Pattern.compile("<a", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
			safeText = scriptPattern.matcher(safeText).replaceAll("");

			// Avoid img tags
			scriptPattern = Pattern.compile("<img>(.*?)</img>",
					Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
			safeText = scriptPattern.matcher(safeText).replaceAll("");

			scriptPattern = Pattern.compile("<img(.*?)>",
					Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
			safeText = scriptPattern.matcher(safeText).replaceAll("");

			scriptPattern = Pattern.compile("<img", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
			safeText = scriptPattern.matcher(safeText).replaceAll("");

			// Avoid iframe tags
			scriptPattern = Pattern.compile("<iframe>(.*?)</iframe>",
					Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
			safeText = scriptPattern.matcher(safeText).replaceAll("");

			scriptPattern = Pattern.compile("<iframe(.*?)>",
					Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
			safeText = scriptPattern.matcher(safeText).replaceAll("");

			scriptPattern = Pattern.compile("<iframe", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
			safeText = scriptPattern.matcher(safeText).replaceAll("");

			// Avoid html patterns
			scriptPattern = Pattern.compile("/>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
			safeText = scriptPattern.matcher(safeText).replaceAll("");

			scriptPattern = Pattern.compile("</", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
			safeText = scriptPattern.matcher(safeText).replaceAll("");

			scriptPattern = Pattern.compile("</(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
			safeText = scriptPattern.matcher(safeText).replaceAll("");

			scriptPattern = Pattern.compile("<(.*?)>(.*?)</(.*?)>",
					Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
			safeText = scriptPattern.matcher(safeText).replaceAll("");

			// Avoid 'src =' html pattern
			scriptPattern = Pattern.compile("src(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
			safeText = scriptPattern.matcher(safeText).replaceAll("");

			// Avoid 'href =' html pattern
			scriptPattern = Pattern.compile("href(.*?)=",
					Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
			safeText = scriptPattern.matcher(safeText).replaceAll("");

			// Avoid script and alerts combined
			scriptPattern = Pattern.compile("(.*?)script(.*?)alert(.*?)script(.*?)",
					Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
			safeText = scriptPattern.matcher(safeText).replaceAll("");

			// Avoid '<javascript:>' in html
			scriptPattern = Pattern.compile("<(.*?)javascript:(.*?)>",
					Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
			safeText = scriptPattern.matcher(safeText).replaceAll("");

			// Avoid '=javascript:' in html
			scriptPattern = Pattern.compile("=(.*?)javascript:",
					Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
			safeText = scriptPattern.matcher(safeText).replaceAll("");

			// Avoid '<javascript:>' in html
			scriptPattern = Pattern.compile("<(.*?)javascript:(.*?)>",
					Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
			safeText = scriptPattern.matcher(safeText).replaceAll("");

			// Avoid '=javascript:' in html
			scriptPattern = Pattern.compile("=(.*?)javascript:",
					Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
			safeText = scriptPattern.matcher(safeText).replaceAll("");

			// Avoid '<javascript:>' in html
			scriptPattern = Pattern.compile("<(.*?)javascript:(.*?)>",
					Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
			safeText = scriptPattern.matcher(safeText).replaceAll("");

			// Avoid '=javascript:' in html
			scriptPattern = Pattern.compile("=(.*?)javascript:",
					Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
			safeText = scriptPattern.matcher(safeText).replaceAll("");

			// Avoid 'style=' in html
			scriptPattern = Pattern.compile("<(.*?)style(.*?)=(.*?)>",
					Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
			safeText = scriptPattern.matcher(safeText).replaceAll("");

			// Avoid style tags
			scriptPattern = Pattern.compile("<style>(.*?)",
					Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
			safeText = scriptPattern.matcher(safeText).replaceAll("");

			scriptPattern = Pattern.compile("<style (.*?)>",
					Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
			safeText = scriptPattern.matcher(safeText).replaceAll("");

			scriptPattern = Pattern.compile("<style>(.*?)</style>",
					Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
			safeText = scriptPattern.matcher(safeText).replaceAll("");

			scriptPattern = Pattern.compile("</style>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
			safeText = scriptPattern.matcher(safeText).replaceAll("");

			// Avoid SVG tags
			scriptPattern = Pattern.compile("<svg>(.*?)",
					Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
			safeText = scriptPattern.matcher(safeText).replaceAll("");

			scriptPattern = Pattern.compile("<svg(.*?)=(.*?)>",
					Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
			safeText = scriptPattern.matcher(safeText).replaceAll("");

			scriptPattern = Pattern.compile("<svg>(.*?)</svg>",
					Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
			safeText = scriptPattern.matcher(safeText).replaceAll("");

			scriptPattern = Pattern.compile("</svg>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
			safeText = scriptPattern.matcher(safeText).replaceAll("");

			// Avoid br tags
			scriptPattern = Pattern.compile("<br>(.*?)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
			safeText = scriptPattern.matcher(safeText).replaceAll("");

			scriptPattern = Pattern.compile("<br(.*?)=(.*?)>",
					Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
			safeText = scriptPattern.matcher(safeText).replaceAll("");

			scriptPattern = Pattern.compile("<br>(.*?)</br>",
					Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
			safeText = scriptPattern.matcher(safeText).replaceAll("");

			scriptPattern = Pattern.compile("</br>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
			safeText = scriptPattern.matcher(safeText).replaceAll("");

			// Avoid META tags
			scriptPattern = Pattern.compile("<META>(.*?)",
					Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
			safeText = scriptPattern.matcher(safeText).replaceAll("");

			scriptPattern = Pattern.compile("<META(.*?)=(.*?)>",
					Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
			safeText = scriptPattern.matcher(safeText).replaceAll("");

			scriptPattern = Pattern.compile("<META>(.*?)</META>",
					Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
			safeText = scriptPattern.matcher(safeText).replaceAll("");

			scriptPattern = Pattern.compile("</META>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
			safeText = scriptPattern.matcher(safeText).replaceAll("");

			// Avoid redirection with urls
			scriptPattern = Pattern.compile("<(.*?)Redirect(.*?)http:>",
					Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
			safeText = scriptPattern.matcher(safeText).replaceAll("");

			// Avoid redirection with urls
			scriptPattern = Pattern.compile("(.*?)Redirect(.*?)http:",
					Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
			safeText = scriptPattern.matcher(safeText).replaceAll("");

			// Avoid redirection with urls
			scriptPattern = Pattern.compile("=(.*?)URL", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
			safeText = scriptPattern.matcher(safeText).replaceAll("");

			// Avoid get http method type calls
			scriptPattern = Pattern.compile("=(.*?)get(.*?);",
					Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
			safeText = scriptPattern.matcher(safeText).replaceAll("");

			// Avoid {{ pattern
			safeText = safeText.replaceAll("\\{\\{","");

			if (input.equalsIgnoreCase(safeText)) {
				return true;
			} else {
				return false;
			}
		}
		return true;

	}
}
