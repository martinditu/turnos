import { useEffect, useState } from "react";
import { ServicioResponseDTO } from "../types/Turno";
import { traerServicios } from "../api/clienteApi";
import useDocumentTitle from "../hooks/useDocumentTitle";

interface Props {
  onSelectServicio: (servicio: ServicioResponseDTO) => void;
}

// lista de servicios disponibles para que el usuario seleccione uno
const ServiciosList = ({ onSelectServicio }: Props) => {
  useDocumentTitle("Servicios disponibles");

  const [servicios, setServicios] = useState<ServicioResponseDTO[]>([]);
  const [cargando, setCargando] = useState(true);

  useEffect(() => {
    const cargarServicios = async () => {
      try {
        const data = await traerServicios();
        setServicios(data);
      } catch (err) {
        console.error("Error al cargar los servicios:", err);
      } finally {
        setCargando(false);
      }
    };

    cargarServicios();
  }, []);

  if (cargando) return <div>Cargando servicios...</div>;

  return (
    <div className="p-4 text-center">
      <h2 className="text-xl font-semibold mb-4">Servicios disponibles</h2>
      <ul className="space-y-2">
        {servicios.map((servicio) => (
          <li key={servicio.id}>
            <button
              onClick={() => onSelectServicio(servicio)}
              className="w-full bg-blue-600 text-white py-2 rounded hover:bg-blue-700 transition"
            >
              {servicio.nombre}
            </button>
          </li>
        ))}
      </ul>
    </div>
  );
};

export default ServiciosList;
