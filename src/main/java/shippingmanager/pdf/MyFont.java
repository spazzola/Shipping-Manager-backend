package shippingmanager.pdf;

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


    public static Font getBoldFont(int size) throws IOException, DocumentException {
        BaseFont bf = BaseFont.createFont("fonts/montserat-bold.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

        return new Font(bf, size);
    }

    public static Font getNormalFont() throws IOException, DocumentException {
        BaseFont bf = BaseFont.createFont("fonts/montserat-regular.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

        return new Font(bf, 8);
    }

}