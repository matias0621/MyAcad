package MyAcad.Project.backend.Service.Academic;
import MyAcad.Project.backend.Enum.AcademicStatus;
import MyAcad.Project.backend.Model.Academic.SubjectsXStudentEntity;
import MyAcad.Project.backend.Model.Academic.SubjectsXStudentResponse;
import MyAcad.Project.backend.Model.Programs.ProgramResponse;
import MyAcad.Project.backend.Service.Programs.ProgramService;
import MyAcad.Project.backend.Service.SubjectsXStudentService;
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
    private final SubjectsXStudentService subjectsXStudentService;

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

        List<SubjectsXStudentResponse> subjects = subjectsXStudentService.getAllSubjectsXStudentByStudentId(student.getId());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4, 50, 50, 60, 50);
        PdfWriter.getInstance(document, baos);

        document.open();

        // ---------- ESTILOS ----------
        Font bold12 = new Font(Font.HELVETICA, 12, Font.BOLD);
        Font bold14 = new Font(Font.HELVETICA, 14, Font.BOLD);
        Font normal12 = new Font(Font.HELVETICA, 12, Font.NORMAL);
        Font normal14 = new Font(Font.HELVETICA, 14, Font.NORMAL);

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

        // COLUMNA IZQUIERDA
        PdfPTable left = new PdfPTable(1);
        left.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        left.addCell(makeCell(logoArgentina, Element.ALIGN_LEFT));
        left.addCell(makeText("República Argentina", bold12, Element.ALIGN_LEFT));
        left.addCell(makeText("Ministerio de Capital Humano", normal12, Element.ALIGN_LEFT));

        PdfPCell leftCell = new PdfPCell(left);
        leftCell.setBorder(Rectangle.NO_BORDER);
        headerTable.addCell(leftCell);

        // COLUMNA DERECHA
        PdfPTable right = new PdfPTable(1);
        right.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        right.addCell(makeCell(logoUTN, Element.ALIGN_RIGHT));
        right.addCell(makeText("Universidad Tecnológica Nacional", bold12, Element.ALIGN_RIGHT));
        right.addCell(makeText("Facultad Regional Mar del Plata", normal12, Element.ALIGN_RIGHT));

        PdfPCell rightCell = new PdfPCell(right);
        rightCell.setBorder(Rectangle.NO_BORDER);
        headerTable.addCell(rightCell);

        headerTable.setSpacingAfter(25f);
        document.add(headerTable);

        // ---------- TÍTULO CON PARTES EN NEGRITA ----------
        Phrase tituloPhrase = new Phrase();

        tituloPhrase.add(new Chunk("Listado de actividad académica de ", normal14));
        tituloPhrase.add(new Chunk(student.getFullName(), bold14)); // nombre en negrita

        tituloPhrase.add(new Chunk(" – DNI: ", normal14));
        tituloPhrase.add(new Chunk(String.valueOf(student.getDni()), bold14)); // dni

        tituloPhrase.add(new Chunk(" - LEGAJO Nº: ", normal14));
        tituloPhrase.add(new Chunk(String.valueOf(student.getLegajo()), bold14)); // legajo

        Paragraph titulo = new Paragraph(tituloPhrase);
        titulo.setAlignment(Element.ALIGN_JUSTIFIED);
        document.add(titulo);

        // ---------- SUBTÍTULO ----------
        document.add(new Paragraph(
                "Para uso y control del estudiante. Documento NO VÁLIDO para certificaciones.",
                normal12
        ));
        document.add(Chunk.NEWLINE);

        // ---------- TABLA DE MATERIAS ----------
        PdfPTable tabla = new PdfPTable(4);
        tabla.setWidthPercentage(100);
        tabla.setWidths(new float[]{60, 20, 50, 25});

        tabla.addCell(makeHeader("Carrera"));
        tabla.addCell(makeHeader("Cuatrimestre"));
        tabla.addCell(makeHeader("Materia"));
        tabla.addCell(makeHeader("Estado"));

        for (SubjectsXStudentResponse m : subjects) {

            tabla.addCell(makeCell(m.getSubjects().getProgram() + ""));
            tabla.addCell(makeCell(String.valueOf(m.getSubjects().getSemesters())));
            tabla.addCell(makeCell(m.getSubjects().getName()));

            String estado = switch (m.getStateStudent()) {
                case FAILED -> "Desaprobada";
                case INPROGRESS -> "Cursando";
                case COMPLETED -> "Cursada";
                case APPROVED -> "Aprobada";
            };

            tabla.addCell(makeCell(estado));
        }

        document.add(tabla);

        document.add(Chunk.NEWLINE);
        document.add(Chunk.NEWLINE);

        // ---------- VALIDACIÓN ----------
        Paragraph valTitle = new Paragraph("VALIDACIÓN", bold12);
        document.add(valTitle);

        document.add(new Paragraph("Descarga directa del sitio de la Facultad", normal12));

        document.add(Chunk.NEWLINE);

        // ---------- PIE CON FECHA Y CIUDAD EN NEGRITA ----------
        String fecha = LocalDate.now().format(DateTimeFormatter.ofPattern("d 'de' MMMM 'de' yyyy"));

        Phrase pie = new Phrase();
        pie.add(new Chunk("Se extiende el presente documento en la ciudad de ", normal12));

        pie.add(new Chunk("Mar del Plata", bold12)); // ciudad bold

        pie.add(new Chunk(" el día ", normal12));

        pie.add(new Chunk(fecha, bold12)); // fecha bold

        pie.add(new Chunk(".", normal12));

        document.add(new Paragraph(pie));

        document.close();
        return baos.toByteArray();
    }


// ---------- FUNCIONES AUXILIARES ----------

    private PdfPCell makeText(String text, Font font, int align) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(align);
        return cell;
    }

    private PdfPCell makeCell(String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text, new Font(Font.HELVETICA, 11)));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        return cell;
    }

    private PdfPCell makeHeader(String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text, new Font(Font.HELVETICA, 11, Font.BOLD)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        return cell;
    }

    private PdfPCell makeCell(Image img, int align) {
        PdfPCell cell = new PdfPCell(img);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(align);
        return cell;
    }



}
