/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package ec.edu.uta.appwebjava.appwebjava.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import ec.edu.uta.appwebjava.appwebjava.utils.ApiClient;
import org.json.JSONArray;

/**
 *
 * @author gabri
 */
public class SvEstudiante extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        JSONArray estudiantes = ApiClient.getEstudiantes();

        request.getSession().setAttribute("estudiantes", estudiantes);
        System.out.println(estudiantes.get(0));
        response.sendRedirect(request.getContextPath() + "/index.jsp");
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");
        boolean ok = false;

        try {
            switch (accion) {
                case "crear":
                    ok = ApiClient.crearEstudiante(
                            request.getParameter("cedula"),
                            request.getParameter("nombre"),
                            request.getParameter("direccion"),
                            request.getParameter("telefono"));
                    break;
                case "editar":
                    // Redirige a un formulario de edición o lo haces inline
                    request.getSession().setAttribute("cedulaEditar", request.getParameter("cedula"));
                    response.sendRedirect(request.getContextPath() + "/editar.jsp");
                    return;
                case "actualizar":
                    ok = ApiClient.actualizarEstudiante(
                            request.getParameter("cedula"),
                            request.getParameter("nombre"),
                            request.getParameter("direccion"),
                            request.getParameter("telefono")
                    );
                    break;
                case "eliminar":
                    ok = ApiClient.eliminarEstudiante(request.getParameter("cedula"));
                    break;
                default:
                // acción desconocida
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        request.getSession().setAttribute("flashMsg", ok ? "Operación exitosa" : "Falló la operación");
        try {
            JSONArray lista = ApiClient.getEstudiantes();
            request.getSession().setAttribute("estudiantes", lista);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        response.sendRedirect(request.getContextPath() + "/index.jsp");
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
