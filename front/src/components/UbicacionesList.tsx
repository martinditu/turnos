import { useEffect, useState } from "react";
import { UbicacionResponseDTO } from "../types/Turno";
import { traerUbicaciones } from "../api/clienteApi"; // renombrado
import useDocumentTitle from "../hooks/useDocumentTitle";

interface Props {
  servicioId: number;
  onSelectUbicacion: (ubicacion: UbicacionResponseDTO) => void;
}

// Lista de ubicaciones disponibles para un servicio
const UbicacionesList = ({ servicioId, onSelectUbicacion }: Props) => {
  useDocumentTitle("UbicacionesList");

  const [ubicaciones, setUbicaciones] = useState<UbicacionResponseDTO[]>([]);
  const [cargando, setCargando] = useState(true);

  useEffect(() => {
    const cargar = async () => {
      try {
        const data = await traerUbicaciones(servicioId);
        setUbicaciones(data);
      } catch (err) {
        console.error("Error al traer ubicaciones", err);
      } finally {
        setCargando(false);
      }
    };
    cargar();
  }, [servicioId]);

  if (cargando) return <div>Cargando ubicaciones...</div>;

  return (
    <div className="p-4 text-center">
      <h3 className="text-xl font-semibold mb-4">Ubicaciones disponibles</h3>
      <ul className="space-y-2">
        {ubicaciones.map((u) => (
          <li key={u.id}>
            <button
              onClick={() => onSelectUbicacion(u)}
              className="w-full bg-blue-600 text-white py-2 rounded hover:bg-blue-700 transition"
            >
              {u.direccion}
            </button>
          </li>
        ))}
      </ul>
    </div>
  );
};

export default UbicacionesList;
