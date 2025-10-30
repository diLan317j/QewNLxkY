// 代码生成时间: 2025-10-30 23:53:17
import io.javalin.Javalin;
import io.javalin.core.util.Header;
import io.javalin.http.ContentType;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.converter.pdf.PdfConverter;
import org.apache.poi.xwpf.converter.pdf.PdfOptions;
import org.eclipse.birt.core.format.DateFormatter;
import org.springframework.http.HttpStatus;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

public class DocumentConverter {
    
    // Define the paths for input and output directories.
    private static final String INPUT_DIRECTORY_PATH = "./input";
    private static final String OUTPUT_DIRECTORY_PATH = "./output";
    
    // Initialize Javalin server.
    private static Javalin app = Javalin.create(config -> {
        config.port(7000); // Set the port number.
    });
    
    // Start the Javalin server.
    public static void main(String[] args) {
        DocumentConverter converter = new DocumentConverter();
        app.routes(() -> {
            // Define the POST endpoint for document conversion.
            app.post("/convert", converter::handleConvertDocument);
        });
        app.start();
    }
    
    // Handle document conversion.
    private void handleConvertDocument(Context ctx) {
        try {
            // Get the uploaded file from the request.
            MultipartFile file = ctx.uploadedFile("file");
            String fileName = file.getFileName();
            String fileExtension = Paths.get(fileName).toString().substring(Paths.get(fileName).toString().lastIndexOf('.') + 1);
            
            // Convert the document based on the file extension.
            switch (fileExtension.toLowerCase()) {
                case "docx":
                    // Convert a DOCX file to PDF.
                    convertDocxToPdf(file, fileName);
                    break;
                // Add more cases for other file formats as needed.
                default:
                    ctx.status(HttpStatus.BAD_REQUEST.value());
                    ctx.result("Unsupported file format");
                    break;
            }
        } catch (IOException e) {
            // Handle exceptions and return an error message.
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR.value());
            ctx.result("Error processing the document: " + e.getMessage());
        }
    }
    
    // Convert a DOCX file to PDF.
    private void convertDocxToPdf(MultipartFile file, String fileName) throws IOException {
        String outputPath = OUTPUT_DIRECTORY_PATH + "/" + new DateFormatter("yyyyMMddHHmm").format(new Date()) + "_" + fileName;
        InputStream docxInputStream = new FileInputStream(file.getJavaPath());
        OutputStream pdfOutputStream = new FileOutputStream(outputPath);
        PdfOptions options = PdfOptions.create();
        PdfConverter.getInstance().convert(docxInputStream, pdfOutputStream, options);
        
        // Set the response content type and stream the output file.
        ctx.contentType(ContentType.PDF);
        ctx.result(outputPath);
    }
}
