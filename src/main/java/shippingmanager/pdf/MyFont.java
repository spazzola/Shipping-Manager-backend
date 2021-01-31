package shippingmanager.pdf;

import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import lombok.NoArgsConstructor;

import java.io.IOException;


@NoArgsConstructor
public class MyFont {

    private static final String REGULAR = "src/main/resources/fonts/montserat-regular.ttf";
    private static final String BOLD = "src/main/resources/fonts/montserat-bold.ttf";


    public static PdfFont getRegularFont() throws IOException {
        FontProgramFactory.createFont(REGULAR);
        return PdfFontFactory.createFont(REGULAR, "Identity-H",true);
    }

    public static PdfFont getBoldFont() throws IOException {
        FontProgramFactory.createFont(BOLD);
        return PdfFontFactory.createFont(BOLD, "Identity-H",true);
    }

}