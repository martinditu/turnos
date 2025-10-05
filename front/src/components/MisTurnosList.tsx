import React, { useEffect, useState } from "react";
import { traerMisTurnos } from "../api/clienteApi";
import { TurnoResponseDTO } from "../types/Turno";
import useDocumentTitle from "../hooks/useDocumentTitle";

export default function MisTurnosList() {
  useDocumentTitle("Mis Turnos");

  const [turnos, setTurnos] = useState<TurnoResponseDTO[]>([]);
  const [error, setError] = useState("");

  useEffect(() => {
    traerMisTurnos()
      .then(setTurnos)
      .catch((err) => setError(err.message || "Error al cargar turnos"));
  }, []);

  if (error) return <div style={{ color: "red" }}>Error: {error}</div>;

  if (turnos.length === 0) {
    return <p>No tenés turnos reservados.</p>;
  }

  return (
    <div style={{ overflowX: "auto", marginTop: "1rem", padding: "1rem" }}>
      <h2
        style={{ fontSize: "1.25rem", fontWeight: "700", marginBottom: "1rem" }}
      >
        Mis turnos reservados
      </h2>
      <table
        style={{
          width: "100%",
          borderCollapse: "collapse",
          minWidth: "700px",
          fontFamily: "Arial, sans-serif",
          fontSize: "14px",
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
            <th style={headerCellStyle}>Código de Anulación</th>
          </tr>
        </thead>
        <tbody>
          {turnos.map((turno, index) => (
            <tr
              key={turno.id}
              style={{
                backgroundColor: index % 2 === 0 ? "#ffffff" : "#f9fafb",
              }}
            >
              <td style={bodyCellStyle}>{turno.id}</td>
              <td style={bodyCellStyle}>{turno.nombreServicio || "-"}</td>
              <td style={bodyCellStyle}>{turno.ubicacionDescripcion || "-"}</td>
              <td style={bodyCellStyle}>{turno.fecha}</td>
              <td style={bodyCellStyle}>{turno.hora}</td>
              <td style={bodyCellStyle}>{turno.nombreEmpleado || "-"}</td>
              <td style={bodyCellStyle}>{turno.codigoAnulacion || "-"}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

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
