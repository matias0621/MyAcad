package MyAcad.Project.backend.Service.Academic;
import MyAcad.Project.backend.Model.Programs.ProgramResponse;
import MyAcad.Project.backend.Service.Programs.ProgramService;
import com.lowagie.text.*;
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

    public byte[] generateCertificate(Student student) throws Exception {
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
        Font titleLarge = new Font(Font.HELVETICA, 16, Font.BOLD);
        Font titleMedium = new Font(Font.HELVETICA, 13, Font.BOLD);
        Font body = new Font(Font.HELVETICA, 12, Font.NORMAL);

        // ---------- ENCABEZADO ----------
        Paragraph p1 = new Paragraph("REPUBLICA ARGENTINA", titleLarge);
        p1.setAlignment(Element.ALIGN_CENTER);
        document.add(p1);

        Paragraph p2 = new Paragraph("Universidad Tecnológica Nacional", titleMedium);
        p2.setAlignment(Element.ALIGN_CENTER);
        document.add(p2);

        Paragraph p3 = new Paragraph("Facultad Regional Mar del Plata", titleMedium);
        p3.setAlignment(Element.ALIGN_CENTER);
        document.add(p3);

        document.add(Chunk.NEWLINE);
        document.add(Chunk.NEWLINE);

        // ---------- TEXTO PRINCIPAL ----------
        // Convertir lista de programas en texto natural
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
