<%
if (request.getSession(false) == null ||
    request.getSession(false).getAttribute("estudiantes") == null) {
    response.sendRedirect(request.getContextPath() + "/SvEstudiante");
    return;
}
%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="org.json.JSONArray"%>
<%@page import="org.json.JSONObject"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Estudiantes</title>
        <script type="text/javascript">
            function confirmarEliminar(cedula) {
                if (confirm("¿Seguro que desea eliminar al estudiante con cédula " + cedula + "?")) {
                    // Crear un formulario
                    var form = document.createElement("form");
                    form.method = "post";
                    form.action = "<%= request.getContextPath() %>/SvEstudiante";
                    var f1 = document.createElement("input");
                    f1.type = "hidden";
                    f1.name = "accion";
                    f1.value = "eliminar";
                    var f2 = document.createElement("input");
                    f2.type = "hidden";
                    f2.name = "cedula";
                    f2.value = cedula;
                    form.appendChild(f1);
                    form.appendChild(f2);
                    document.body.appendChild(form);
                    form.submit();
                }
            }
        </script>
    </head>
    <body>
        <h1>Lista de estudiantes</h1>
        <table border="1">
            <tr>
                <th># Est.</th>
                <th>Cédula</th>
                <th>Nombre</th>
                <th>Dirección</th>
                <th>Teléfono</th>
                <th colspan="2">Acciones</th>
            </tr>
            <% 
                JSONArray estudiantes = (JSONArray) request.getSession().getAttribute("estudiantes");
                if (estudiantes != null) {
                    for (int i = 0; i < estudiantes.length(); i++) {
                        JSONObject e = estudiantes.getJSONObject(i);
            %>
            <tr>
                <td><%= i+1 %></td>
                <td><%= e.optString("cedula") %></td>
                <td><%= e.optString("nombre") %></td>
                <td><%= e.optString("direccion") %></td>
                <td><%= e.optString("telefono") %></td>
                <td>
                    <!-- Botón Editar -->
                    <form method="post" action="<%= request.getContextPath() %>/SvEstudiante" style="display:inline">
                        <input type="hidden" name="accion" value="editar"/>
                        <input type="hidden" name="cedula" value="<%= e.optString("cedula") %>"/>
                        <button type="submit">Editar</button>
                    </form>
                    &nbsp;
                    <!-- Botón Eliminar -->
                    <button type="button" onclick="confirmarEliminar('<%= e.optString("cedula") %>')">Eliminar</button>
                </td>
            </tr>
            <%
                    }
                } else {
            %>
            <tr><td colspan="5">No se encuentran estudiantes</td></tr>
            <%
                }
            %>
        </table>
        <h2>Nuevo estudiante</h2>
        <form method="post" action="${pageContext.request.contextPath}/SvEstudiante">
            <input type="hidden" name="accion" value="crear"/>
            <p><label>Cédula:<input name="cedula" required /></label></p>
            <p><label>Nombre:<input name="nombre" required /></label></p>
            <p><label>Dirección:<input name="direccion" required /></label></p>
            <p><label>Teléfono:<input name="telefono" required /></label></p>
            <button type="submit">Guardar</button>
        </form>

        <%-- Mensaje --%>
        <%
          String msg = (String) session.getAttribute("flashMsg");
          if (msg != null) {
        %>
        <p><%= msg %></p>
        <%
            session.removeAttribute("flashMsg");
          }
        %>

    </body>
</html>
