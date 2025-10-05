import { ClienteAdminDTO } from "../types/Cliente";

interface Props {
  activos: ClienteAdminDTO[];
  dadosDeBaja: ClienteAdminDTO[];
  onDarDeBaja: (id: number) => void;
  onDarDeAlta: (id: number) => void;
  onEditar: (cliente: ClienteAdminDTO) => void;
}

export default function ClienteAdminList({
  activos,
  dadosDeBaja,
  onDarDeBaja,
  onDarDeAlta,
  onEditar,
}: Props) {
  return (
    <div style={{ overflowX: "auto", padding: "1rem" }}>
      <h2 style={sectionTitle}>Clientes activos</h2>
      {activos.length > 0 ? (
        <Tabla
          clientes={activos}
          esActivo
          onDarDeBaja={onDarDeBaja}
          onEditar={onEditar}
        />
      ) : (
        <p>No hay clientes activos.</p>
      )}

      <h2 style={{ ...sectionTitle, marginTop: "2rem" }}>
        Clientes dados de baja
      </h2>
      {dadosDeBaja.length > 0 ? (
        <Tabla clientes={dadosDeBaja} onDarDeAlta={onDarDeAlta} />
      ) : (
        <p>No hay clientes dados de baja.</p>
      )}
    </div>
  );
}

type TablaProps = {
  clientes: ClienteAdminDTO[];
  esActivo?: boolean;
  onDarDeBaja?: (id: number) => void;
  onDarDeAlta?: (id: number) => void;
  onEditar?: (cliente: ClienteAdminDTO) => void;
};

function Tabla({
  clientes,
  esActivo = false,
  onDarDeBaja,
  onDarDeAlta,
  onEditar,
}: TablaProps) {
  return (
    <table
      style={{
        width: "100%",
        minWidth: "900px",
        borderCollapse: "collapse",
        fontFamily: "Arial, sans-serif",
        fontSize: "14px",
        marginTop: "1rem",
      }}
    >
      <thead>
        <tr style={{ backgroundColor: "#f3f4f6", textAlign: "center" }}>
          <th style={headerCellStyle}>ID</th>
          <th style={headerCellStyle}>Email</th>
          <th style={headerCellStyle}>Activo</th>
          <th style={headerCellStyle}>Nombre</th>
          <th style={headerCellStyle}>Apellido</th>
          <th style={headerCellStyle}>DNI</th>
          <th style={headerCellStyle}>Acciones</th>
        </tr>
      </thead>
      <tbody>
        {clientes.map((cliente, index) => (
          <tr
            key={cliente.id}
            style={{
              backgroundColor: index % 2 === 0 ? "#ffffff" : "#f9fafb",
              textAlign: "center",
            }}
          >
            <td style={bodyCellStyle}>{cliente.id}</td>
            <td style={bodyCellStyle}>{cliente.email}</td>
            <td style={bodyCellStyle}>{cliente.clienteActivo ? "Sí" : "No"}</td>
            <td style={bodyCellStyle}>{cliente.nombre}</td>
            <td style={bodyCellStyle}>{cliente.apellido}</td>
            <td style={bodyCellStyle}>{cliente.dni}</td>
            <td style={bodyCellStyle}>
              {esActivo ? (
                <>
                  <button
                    style={buttonStyle("red")}
                    onClick={() => onDarDeBaja?.(cliente.id)}
                  >
                    Dar de baja
                  </button>{" "}
                  <button
                    style={buttonStyle("blue")}
                    onClick={() => onEditar?.(cliente)}
                  >
                    Editar
                  </button>
                </>
              ) : (
                <button
                  style={buttonStyle("green")}
                  onClick={() => onDarDeAlta?.(cliente.id)}
                >
                  Dar de alta
                </button>
              )}
            </td>
          </tr>
        ))}
      </tbody>
    </table>
  );
}

// 🧱 Estilos base
const sectionTitle: React.CSSProperties = {
  fontSize: "1.25rem",
  fontWeight: "700",
  marginBottom: "0.5rem",
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
  color: "#1f2937",
  verticalAlign: "middle",
};

// 🎨 Botón con color variable
const buttonStyle = (color: "red" | "blue" | "green"): React.CSSProperties => ({
  backgroundColor:
    color === "red" ? "#ef4444" : color === "blue" ? "#3b82f6" : "#10b981", // green
  color: "#ffffff",
  border: "none",
  padding: "6px 10px",
  borderRadius: "4px",
  cursor: "pointer",
  margin: "0 4px",
});
