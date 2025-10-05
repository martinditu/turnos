import { TurnoResponseDTO } from "../types/Turno";

interface Props {
  turnos: TurnoResponseDTO[];
}

// Lista de turnos visibles para el administrador
const TurnoAdminList = ({ turnos }: Props) => {
  if (turnos.length === 0) {
    return <p>No hay turnos disponibles.</p>;
  }

  return (
    <div style={{ overflowX: "auto", marginTop: "1rem" }}>
      <table
        style={{
          width: "100%",
          borderCollapse: "collapse",
          fontFamily: "Arial, sans-serif",
          fontSize: "14px",
          minWidth: "900px",
        }}
      >
        <thead>
          <tr style={{ backgroundColor: "#f3f4f6", textAlign: "center" }}>
            <th style={headerCellStyle}>ID</th>
            <th style={headerCellStyle}>Servicio</th>
            <th style={headerCellStyle}>Ubicación</th>
            <th style={headerCellStyle}>Fecha</th>
            <th style={headerCellStyle}>Hora</th>
            <th style={headerCellStyle}>Empleado</th>
            <th style={headerCellStyle}>Cliente</th>
            <th style={headerCellStyle}>Codigo Anulacion</th>
          </tr>
        </thead>
        <tbody>
          {turnos.map((turno, index) => (
            <tr
              key={turno.id}
              style={{
                backgroundColor: index % 2 === 0 ? "#ffffff" : "#f9fafb",
                cursor: "default",
              }}
            >
              <td style={bodyCellStyle}>{turno.id}</td>
              <td style={bodyCellStyle}>
                {turno.nombreServicio ?? "Sin servicio"}
              </td>
              <td style={bodyCellStyle}>
                {turno.ubicacionDescripcion ?? "Sin ubicación"}
              </td>
              <td style={bodyCellStyle}>{turno.fecha}</td>
              <td style={bodyCellStyle}>{turno.hora}</td>
              <td style={bodyCellStyle}>
                {turno.nombreEmpleado ?? "Sin empleado"}
              </td>
              <td style={bodyCellStyle}>
                {turno.nombreCliente ?? "Sin cliente"}
              </td>
              <td style={bodyCellStyle}>{turno.codigoAnulacion ?? "-"}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

const headerCellStyle: React.CSSProperties = {
  padding: "10px 15px",
  border: "1px solid #d1d5db",
  fontWeight: "600",
  color: "#374151",
};

const bodyCellStyle: React.CSSProperties = {
  padding: "10px 15px",
  border: "1px solid #e5e7eb",
  textAlign: "center",
  color: "#1f2937",
  verticalAlign: "middle",
};

export default TurnoAdminList;
