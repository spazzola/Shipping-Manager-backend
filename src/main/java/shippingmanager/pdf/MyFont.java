package shippingmanager.pdf;

import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import lombok.NoArgsConstructor;

import java.io.IOException;


@NoArgsConstructor
public class MyFont {

    public static final String REGULAR = "src/main/resources/fonts/montserat-regular.ttf";
    public static final String BOLD = "src/main/resources/fonts/montserat-bold.ttf";


    public static PdfFont getRegularFont() throws IOException {
        FontProgramFactory.createFont(REGULAR);
        return PdfFontFactory.createFont(REGULAR, "Identity-H",true);
    }

    public static PdfFont getBolderFont() throws IOException {
        FontProgramFactory.createFont(BOLD);
        return PdfFontFactory.createFont(BOLD, "Identity-H",true);
    }

}