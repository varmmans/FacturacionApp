package servletClient;

import com.hanseg.facturacion.msfacturas002.ejb.FacturaEJBBean;
import com.hanseg.facturacion.msfacturas002.entity.Documento;
import com.hanseg.facturacion.msfacturas002.enumeration.ComprobanteEnum;

import java.io.IOException;
import java.io.PrintWriter;

import java.util.List;

import javax.ejb.EJB;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "pendientes", urlPatterns = { "pendientes" })
public class Pendientes extends HttpServlet {
    private static final String CONTENT_TYPE = "text/html; charset=windows-1252";
    @EJB
    private FacturaEJBBean facturaEJBBean;

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType(CONTENT_TYPE);
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<head><title>Comprobantes Pendientes</title>");
        out.println("<style>table {border-collapse: collapse;}");
        out.println("table, th, td {border: 1px solid gray; font-size:11px;}</style>");
        out.println("</head>");
        out.println("<body style=\"font-family:arial,helvetica,sans-serif;\" >");
        try {
            out.println("<h3>Comprobantes Pendientes</h3>");
            printDocumento(out, facturaEJBBean.getDocumentoFindxStatus(ComprobanteEnum.PENDIENTE.getValue()));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        out.println("</body></html>");
        out.close();
    }

    public static void printDocumento(PrintWriter out, List<Documento> documentos) {
        if (documentos.size() > 0) {
            beginTable(out, documentos.get(0)
                                      .getClass()
                                      .getSimpleName());
            out.println("<tr>");
            printHeader(out, "Archivo");
            printHeader(out, "Acceso");
            printHeader(out, "Documento");
            printHeader(out, "Empresa");
            printHeader(out, "Interno");
            printHeader(out, "Respuesta SRI");
            printHeader(out, "Usuario");
            printHeader(out, "Fecha Respuesta Aut SRI");
            printHeader(out, "Fecha Solicitud Aut SRI");
            printHeader(out, "Fecha Solicitud Rec SRI");
            printHeader(out, "Fecha Envio Cliente");
            printHeader(out, "Fecha Modifica");
            printHeader(out, "Fecha Creacion");
            printHeader(out, "Fecha Respuesta Rec SRI");
            //printHeader(out, "Firma");
            printHeader(out, "Ambiente");
            printHeader(out, "Status A");
            printHeader(out, "Status C");
            printHeader(out, "Status E");
            printHeader(out, "Status R");
            printHeader(out, "Status");
            printHeader(out, "Tipo Documento");
            printHeader(out, "Modificado por");
            printHeader(out, "Creado por");
            out.println("</tr>");
            for (Documento documento : documentos) {
                printDocumentoData(out, documento);
            }
            out.println("</table>");
        } else {
            out.print("0 Rows");
        }
    }

    public static void printDocumentoData(PrintWriter out, Documento documento) {
        out.println("<tr>");
        printRow(out, documento.getNombreArchivoDoc());
        printRow(out, documento.getClaveAccesoDoc());
        printRow(out, documento.getCodigoDocumento());
        printRow(out, documento.getCodigoEmpresa());
        printRow(out, documento.getCodigoInterno());
        printRow(out, documento.getCodigoRespuestaSRI());
        printRow(out, documento.getCodigoUsuario());
        printRow(out, documento.getFechaRespuestaAutSRI());
        printRow(out, documento.getFechaSolicitudAutSRI());
        printRow(out, documento.getFechaSolicitudRecSRI());
        printRow(out, documento.getFechaEnvioCliente());
        printRow(out, documento.getFechaModificaReg());
        printRow(out, documento.getFechaCreacionReg());
        printRow(out, documento.getFechaRespuestaRecSRI());
        //printRow(out, documento.getNombreArchivoFirma());
        printRow(out, documento.getCodigoAmbiente());
        printRow(out, documento.getStatusAutorizado());
        printRow(out, documento.getStatusCliente());
        printRow(out, documento.getStatusEnvio());
        printRow(out, documento.getStatusRecibido());
        printRow(out, documento.getStatus());
        printRow(out, documento.getTipoDocumento());
        printRow(out, documento.getUsuarioModificaReg());
        printRow(out, documento.getUsuarioCreacionReg());
        out.println("</tr>");
    }

    public static void beginTable(PrintWriter out, String header) {
        out.println("<table>");
        //out.println("<caption><h2>" + header + "</h2></>");
    }

    public static void printHeader(PrintWriter out, String colHeader) {
        out.println("<th>" + colHeader + "</th>");
    }

    public static void printRow(PrintWriter out, Object val) {
        out.println("<td>");
        if (val instanceof List) {
            out.print("List Of Values");
        } else {
            out.print(val);
        }
        out.println("</td>");
    }

}
