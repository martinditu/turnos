import useDocumentTitle from "../hooks/useDocumentTitle";
import useDiasDisponibles from "../hooks/useDiasDisponibles";

interface Props {
  servicioId: number;
  onSelectFecha: (fecha: string) => void;
}

function ListDiasDisponibles({ servicioId, onSelectFecha }: Props) {
  useDocumentTitle("Dias Disponibles");

  // hook para obtener los dias disponibles desde el back
  const { dias, cargando, error } = useDiasDisponibles(servicioId);

  if (cargando) return <div>Cargando dias disponibles...</div>;
  if (error) return <div style={{ color: "red" }}>{error}</div>;
  if (dias.length === 0) return <div>No hay dias disponibles.</div>;

  return (
    <div>
      <h3>Fechas disponibles</h3>
      <ul>
        {dias.map((dia) => (
          <li key={dia}>
            <button onClick={() => onSelectFecha(dia)}>
              {new Date(dia).toLocaleDateString()}
            </button>
          </li>
        ))}
      </ul>
    </div>
  );
}

export default ListDiasDisponibles;
