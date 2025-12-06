package MyAcad.Project.backend.Service.Academic;
import MyAcad.Project.backend.Model.Programs.ProgramResponse;
import MyAcad.Project.backend.Service.Programs.ProgramService;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import MyAcad.Project.backend.Model.Users.Student;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.lowagie.text.pdf.PdfWriter;

@Service
@RequiredArgsConstructor
public class CertificateService {
    private final ProgramService programService;

    public byte[] generateRegularStudentCertificate(Student student) throws Exception {

        List<ProgramResponse> studentPrograms = programService.findByStudent(student.getId());
        List<String> programs = studentPrograms.stream().map(ProgramResponse::getName).toList();

        if (programs.isEmpty()) {
            throw new Exception("El alumno no tiene programas asignados");
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4, 50, 50, 60, 50);
        PdfWriter.getInstance(document, baos);

        document.open();

        // ---------- ESTILOS ----------
        Font headerBold = new Font(Font.HELVETICA, 12, Font.BOLD);
        Font headerNormal = new Font(Font.HELVETICA, 12, Font.NORMAL);
        Font titleMedium = new Font(Font.HELVETICA, 13, Font.BOLD);
        Font body = new Font(Font.HELVETICA, 12, Font.NORMAL);

        // ---------- LOGOS ----------
        Image logoArgentina = Image.getInstance(
                getClass().getClassLoader().getResource("Media/Escudo-republica-argentina.png")
        );

        Image logoUTN = Image.getInstance(
                getClass().getClassLoader().getResource("Media/Logo.png")
        );

        logoArgentina.scaleToFit(80, 80);
        logoUTN.scaleToFit(80, 80);

        PdfPTable headerTable = new PdfPTable(2);
        headerTable.setWidthPercentage(100);
        headerTable.setWidths(new float[]{50, 50});
        headerTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);

        // --------- COLUMNA IZQUIERDA ---------
        PdfPTable leftTable = new PdfPTable(1);
        leftTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);

        PdfPCell leftLogo = new PdfPCell(logoArgentina);
        leftLogo.setBorder(Rectangle.NO_BORDER);
        leftLogo.setHorizontalAlignment(Element.ALIGN_LEFT);
        leftTable.addCell(leftLogo);

        PdfPCell arg1 = new PdfPCell(new Phrase("República Argentina", headerBold));
        arg1.setBorder(Rectangle.NO_BORDER);
        arg1.setHorizontalAlignment(Element.ALIGN_LEFT);
        leftTable.addCell(arg1);

        PdfPCell arg2 = new PdfPCell(new Phrase("Ministerio de Capital Humano", headerNormal));
        arg2.setBorder(Rectangle.NO_BORDER);
        arg2.setHorizontalAlignment(Element.ALIGN_LEFT);
        leftTable.addCell(arg2);

        PdfPCell leftCell = new PdfPCell(leftTable);
        leftCell.setBorder(Rectangle.NO_BORDER);
        headerTable.addCell(leftCell);

        // --------- COLUMNA DERECHA ---------
        PdfPTable rightTable = new PdfPTable(1);
        rightTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);

        PdfPCell rightLogo = new PdfPCell(logoUTN);
        rightLogo.setBorder(Rectangle.NO_BORDER);
        rightLogo.setHorizontalAlignment(Element.ALIGN_RIGHT);
        rightTable.addCell(rightLogo);

        PdfPCell utn1 = new PdfPCell(new Phrase("Universidad Tecnológica Nacional", headerBold));
        utn1.setBorder(Rectangle.NO_BORDER);
        utn1.setHorizontalAlignment(Element.ALIGN_RIGHT);
        rightTable.addCell(utn1);

        PdfPCell utn2 = new PdfPCell(new Phrase("Facultad Regional Mar del Plata", headerNormal));
        utn2.setBorder(Rectangle.NO_BORDER);
        utn2.setHorizontalAlignment(Element.ALIGN_RIGHT);
        rightTable.addCell(utn2);

        PdfPCell rightCell = new PdfPCell(rightTable);
        rightCell.setBorder(Rectangle.NO_BORDER);
        headerTable.addCell(rightCell);

        headerTable.setSpacingAfter(35f);
        document.add(headerTable);

        document.add(Chunk.NEWLINE);
        document.add(Chunk.NEWLINE);
        document.add(Chunk.NEWLINE);

        // ---------- TEXTO PRINCIPAL ----------
        String programasTexto;

        if (programs.size() == 1) {
            programasTexto = programs.get(0);
        } else if (programs.size() == 2) {
            programasTexto = programs.get(0) + " y " + programs.get(1);
        } else {
            programasTexto = String.join(", ", programs.subList(0, programs.size() - 1))
                    + " y " + programs.get(programs.size() - 1);
        }

        String palabraEspecialidad = (programs.size() > 1) ? "las especialidades" : "la especialidad";
        String verboDictar = (programs.size() > 1) ? "se dictan" : "se dicta";

        Font bold = new Font(Font.HELVETICA, 12, Font.BOLD);
        Font normal = new Font(Font.HELVETICA, 12, Font.NORMAL);

        Phrase cuerpo = new Phrase();

        cuerpo.add(new Chunk("Por la presente se hace constar que ", normal));


        cuerpo.add(new Chunk(student.getFullName(), bold));

        cuerpo.add(new Chunk(" – DNI: " + student.getDni() + " - LEGAJO Nº: " + student.getLegajo() + ", es ", normal));

        cuerpo.add(new Chunk("Estudiante Regular", bold));
        cuerpo.add(new Chunk(" de " + palabraEspecialidad + " ", normal));

        cuerpo.add(new Chunk(programasTexto, bold));

        cuerpo.add(new Chunk(" que " + verboDictar + " en la Facultad Regional ", normal));

        cuerpo.add(new Chunk("Mar del Plata", bold));

        cuerpo.add(new Chunk(" de la Universidad Tecnológica Nacional.\n\n", normal));

        cuerpo.add(new Chunk("A solicitud del interesado y a los fines de ser presentado ante quien corresponda, se le extiende el presente certificado, sin enmiendas ni raspaduras, en ", normal));

        cuerpo.add(new Chunk("Mar del Plata", bold));
        cuerpo.add(new Chunk(" el ", normal));

        String fecha = LocalDate.now().format(DateTimeFormatter.ofPattern("d 'de' MMMM 'de' yyyy"));
        cuerpo.add(new Chunk(fecha, bold));
        cuerpo.add(new Chunk(".-", normal));

        Paragraph cuerpoParagraph = new Paragraph(cuerpo);
        cuerpoParagraph.setAlignment(Element.ALIGN_JUSTIFIED);
        document.add(cuerpoParagraph);

        document.add(Chunk.NEWLINE);
        document.add(Chunk.NEWLINE);

        // ---------- VALIDACIÓN ----------
        Font valFont = new Font(Font.HELVETICA, 12, Font.BOLD);
        Paragraph valTitle = new Paragraph("VALIDACIÓN", valFont);
        valTitle.setAlignment(Element.ALIGN_LEFT);
        document.add(valTitle);

        Paragraph valText = new Paragraph(
                "Descarga directa del sitio de la Facultad",
                body
        );
        valText.setAlignment(Element.ALIGN_LEFT);
        document.add(valText);

        document.close();
        return baos.toByteArray();
    }

    public byte[] generateAcademicActivityCertificate(Student student) throws Exception {

        List<ProgramResponse> studentPrograms = programService.findByStudent(student.getId());
        List<String> programs = studentPrograms.stream().map(ProgramResponse::getName).toList();

        if (programs.isEmpty()) {
            throw new Exception("El alumno no tiene programas asignados");
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4, 50, 50, 60, 50);
        PdfWriter.getInstance(document, baos);

        document.open();

        // ---------- ESTILOS ----------
        Font headerBold = new Font(Font.HELVETICA, 12, Font.BOLD);
        Font headerNormal = new Font(Font.HELVETICA, 12, Font.NORMAL);
        Font titleMedium = new Font(Font.HELVETICA, 13, Font.BOLD);
        Font body = new Font(Font.HELVETICA, 12, Font.NORMAL);

        // ---------- LOGOS ----------
        Image logoArgentina = Image.getInstance(
                getClass().getClassLoader().getResource("Media/Escudo-republica-argentina.png")
        );

        Image logoUTN = Image.getInstance(
                getClass().getClassLoader().getResource("Media/Logo.png")
        );

        logoArgentina.scaleToFit(80, 80);
        logoUTN.scaleToFit(80, 80);

        PdfPTable headerTable = new PdfPTable(2);
        headerTable.setWidthPercentage(100);
        headerTable.setWidths(new float[]{50, 50});
        headerTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);

        // --------- COLUMNA IZQUIERDA ---------
        PdfPTable leftTable = new PdfPTable(1);
        leftTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);

        PdfPCell leftLogo = new PdfPCell(logoArgentina);
        leftLogo.setBorder(Rectangle.NO_BORDER);
        leftLogo.setHorizontalAlignment(Element.ALIGN_LEFT);
        leftTable.addCell(leftLogo);

        PdfPCell arg1 = new PdfPCell(new Phrase("República Argentina", headerBold));
        arg1.setBorder(Rectangle.NO_BORDER);
        arg1.setHorizontalAlignment(Element.ALIGN_LEFT);
        leftTable.addCell(arg1);

        PdfPCell arg2 = new PdfPCell(new Phrase("Ministerio de Capital Humano", headerNormal));
        arg2.setBorder(Rectangle.NO_BORDER);
        arg2.setHorizontalAlignment(Element.ALIGN_LEFT);
        leftTable.addCell(arg2);

        PdfPCell leftCell = new PdfPCell(leftTable);
        leftCell.setBorder(Rectangle.NO_BORDER);
        headerTable.addCell(leftCell);

        // --------- COLUMNA DERECHA ---------
        PdfPTable rightTable = new PdfPTable(1);
        rightTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);

        PdfPCell rightLogo = new PdfPCell(logoUTN);
        rightLogo.setBorder(Rectangle.NO_BORDER);
        rightLogo.setHorizontalAlignment(Element.ALIGN_RIGHT);
        rightTable.addCell(rightLogo);

        PdfPCell utn1 = new PdfPCell(new Phrase("Universidad Tecnológica Nacional", headerBold));
        utn1.setBorder(Rectangle.NO_BORDER);
        utn1.setHorizontalAlignment(Element.ALIGN_RIGHT);
        rightTable.addCell(utn1);

        PdfPCell utn2 = new PdfPCell(new Phrase("Facultad Regional Mar del Plata", headerNormal));
        utn2.setBorder(Rectangle.NO_BORDER);
        utn2.setHorizontalAlignment(Element.ALIGN_RIGHT);
        rightTable.addCell(utn2);

        PdfPCell rightCell = new PdfPCell(rightTable);
        rightCell.setBorder(Rectangle.NO_BORDER);
        headerTable.addCell(rightCell);

        document.add(headerTable);

        document.add(Chunk.NEWLINE);
        document.add(Chunk.NEWLINE);

        // ---------- TEXTO PRINCIPAL ----------
        String programasTexto;

        if (programs.size() == 1) {
            programasTexto = programs.get(0);
        } else if (programs.size() == 2) {
            programasTexto = programs.get(0) + " y " + programs.get(1);
        } else {
            programasTexto = String.join(", ", programs.subList(0, programs.size() - 1))
                    + " y " + programs.get(programs.size() - 1);
        }

        String palabraEspecialidad = (programs.size() > 1) ? "las especialidades" : "la especialidad";
        String verboDictar = (programs.size() > 1) ? "se dictan" : "se dicta";

        String cuerpo = String.format(
                "Por la presente se hace constar que %s – DNI: %s - LEGAJO Nº: %s, "
                        + "es Estudiante Regular de %s %s que %s en la "
                        + "Facultad Regional Mar del Plata de la Universidad Tecnológica Nacional.\n\n"
                        + "A solicitud del interesado y a los fines de ser presentado ante quien "
                        + "corresponda, se le extiende el presente certificado, sin enmiendas ni raspaduras, "
                        + "en Mar del Plata el %s.-",
                student.getFullName(),
                student.getDni(),
                student.getLegajo(),
                palabraEspecialidad,
                programasTexto,
                verboDictar,
                LocalDate.now().format(DateTimeFormatter.ofPattern("d 'de' MMMM 'de' yyyy"))
        );

        Paragraph cuerpoParagraph = new Paragraph(cuerpo, body);
        cuerpoParagraph.setAlignment(Element.ALIGN_JUSTIFIED);
        document.add(cuerpoParagraph);

        document.add(Chunk.NEWLINE);
        document.add(Chunk.NEWLINE);
        document.add(Chunk.NEWLINE);

        // ---------- VALIDACIÓN ----------
        Font valFont = new Font(Font.HELVETICA, 12, Font.BOLD);
        Paragraph valTitle = new Paragraph("VALIDACIÓN", valFont);
        valTitle.setAlignment(Element.ALIGN_LEFT);
        document.add(valTitle);

        Paragraph valText = new Paragraph(
                "Descarga directa del sitio de la Facultad",
                body
        );
        valText.setAlignment(Element.ALIGN_LEFT);
        document.add(valText);

        document.close();
        return baos.toByteArray();
    }


}
