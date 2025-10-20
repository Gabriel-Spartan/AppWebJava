<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="org.json.JSONArray, org.json.JSONObject" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Editar Estudiante</title>
    </head>
    <body>
        <h1>Editar Estudiante</h1>
        <%
           String cedulaEdit = (String) session.getAttribute("cedulaEditar");
           JSONArray estudiantes = (JSONArray) session.getAttribute("estudiantes");
           JSONObject seleccionado = null;
           if (cedulaEdit != null && estudiantes != null) {
             for (int i = 0; i < estudiantes.length(); i++) {
               JSONObject e = estudiantes.getJSONObject(i);
               if (cedulaEdit.equals(e.optString("cedula"))) {
                 seleccionado = e;
                 break;
               }
             }
           }
           if (seleccionado == null) {
        %>
        <p>Estudiante no encontrado</p>
        <%
           } else {
        %>
        <form method="post" action="<%= request.getContextPath() %>/SvEstudiante">
            <input type="hidden" name="accion" value="actualizar"/>
            <input type="hidden" name="cedula" value="<%= seleccionado.optString("cedula") %>" />
            <p><label>Nombre:<input name="nombre" value="<%= seleccionado.optString("nombre") %>" required/></label></p>
            <p><label>Dirección:<input name="direccion" value="<%= seleccionado.optString("direccion") %>" required/></label></p>
            <p><label>Teléfono:<input name="telefono" value="<%= seleccionado.optString("telefono") %>" required/></label></p>
            <button type="submit">Guardar Cambios</button>
        </form>
        <%
           }
        %>
    </body>
</html>
