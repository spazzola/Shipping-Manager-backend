package shippingmanager.pdf;

import com.itextpdf.io.font.FontProgram;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.pdf.BaseFont;
import lombok.NoArgsConstructor;

import java.io.IOException;

import static com.itextpdf.text.FontFactory.registerDirectory;

@NoArgsConstructor
public class MyFont {

    public static final String REGULAR = "src/main/resources/fonts/montserat-regular.ttf";
    public static final String BOLD = "src/main/resources/fonts/montserat-bold.ttf";

    public static Font getBoldFont(int size) throws IOException, DocumentException {
        BaseFont bf = BaseFont.createFont("fonts/montserat-bold.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

        return new Font(bf, size);
    }

    public static Font getNormalFont() throws IOException, DocumentException {
        BaseFont bf = BaseFont.createFont("fonts/montserat-regular.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

        return new Font(bf, 8);
    }

    public static PdfFont getRegularFont() throws IOException {
        FontProgramFactory.createFont(REGULAR);
        return PdfFontFactory.createFont(REGULAR, "Identity-H",true);
    }

    public static PdfFont getBolderFont() throws IOException {
        FontProgramFactory.createFont(BOLD);
        return PdfFontFactory.createFont(BOLD, "Identity-H",true);
    }

}