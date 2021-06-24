package shippingmanager.pdf;

import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import lombok.NoArgsConstructor;

import java.io.IOException;


@NoArgsConstructor
public class MyFont {

    //private static final String REGULAR = "src/main/resources/fonts/montserat-regular.ttf";
    //private static final String BOLD = "src/main/resources/fonts/montserat-bold.ttf";


    public static PdfFont getRegularFont() throws IOException {
        String path = loadRegularFontPath();
        FontProgramFactory.createFont(path);

        return PdfFontFactory.createFont(path, "Identity-H",true);
    }

    public static PdfFont getBoldFont() throws IOException {
        String path = loadBoldFontPath();
        FontProgramFactory.createFont(path);
        return PdfFontFactory.createFont(path, "Identity-H",true);
    }

    private static String loadRegularFontPath() {
        return MyFont.class.getClassLoader().getResource("fonts/montserat-regular.ttf").getPath();
    }

    private static String loadBoldFontPath() {
        return MyFont.class.getClassLoader().getResource("fonts/montserat-bold.ttf").getPath();
    }
}