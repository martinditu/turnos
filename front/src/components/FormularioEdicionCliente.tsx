import React, { useState } from "react";
import { ClienteAdminDTO } from "../types/Cliente";

interface Props {
  cliente: ClienteAdminDTO;
  onGuardar: (clienteEditado: ClienteAdminDTO) => void;
  onCancelar: () => void;
}

// form para editar datos de un cliente
const FormularioEdicionCliente: React.FC<Props> = ({
  cliente,
  onGuardar,
  onCancelar,
}) => {
  const [formulario, setFormulario] = useState<ClienteAdminDTO>({ ...cliente });

  const manejarCambio = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormulario((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const manejarEnvio = (e: React.FormEvent) => {
    e.preventDefault();
    onGuardar(formulario);
  };

  return (
    <div className="modal">
      <form
        onSubmit={manejarEnvio}
        className="p-4 bg-white rounded shadow-md max-w-md mx-auto"
      >
        <h2 className="text-xl font-semibold mb-4">Editar cliente</h2>

        <div className="mb-3">
          <label>Email:</label>
          <input
            name="email"
            value={formulario.email}
            onChange={manejarCambio}
            className="border px-2 py-1 w-full"
          />
        </div>

        <div className="mb-3">
          <label>Nombre:</label>
          <input
            name="nombre"
            value={formulario.nombre}
            onChange={manejarCambio}
            className="border px-2 py-1 w-full"
          />
        </div>

        <div className="mb-3">
          <label>Apellido:</label>
          <input
            name="apellido"
            value={formulario.apellido}
            onChange={manejarCambio}
            className="border px-2 py-1 w-full"
          />
        </div>

        <div className="mb-3">
          <label>DNI:</label>
          <input
            name="dni"
            value={formulario.dni}
            onChange={manejarCambio}
            className="border px-2 py-1 w-full"
          />
        </div>

        <div className="mt-4 flex justify-end gap-2">
          <button
            type="button"
            onClick={onCancelar}
            className="bg-gray-400 text-white px-4 py-2 rounded"
          >
            Cancelar
          </button>
          <button
            type="submit"
            className="bg-blue-600 text-white px-4 py-2 rounded"
          >
            Guardar
          </button>
        </div>
      </form>
    </div>
  );
};

export default FormularioEdicionCliente;
