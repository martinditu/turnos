import MenuBase from "./MenuBase";
import useDocumentTitle from "../../hooks/useDocumentTitle";

// menu principal para cliente
function MenuCliente() {
  useDocumentTitle("MenuCliente");

  const options = [
    {
      label: "Reservar turno",
      path: "/dashboard/cliente/reservar",
      colorClass: "bg-blue-600 hover:bg-blue-700",
    },
    {
      label: "Mis turnos",
      path: "/dashboard/cliente/mis-turnos",
      colorClass: "bg-green-600 hover:bg-green-700",
    },
    {
      label: "Anular turno",
      path: "/dashboard/cliente/anular",
      colorClass: "bg-yellow-600 hover:bg-yellow-700",
    },
  ];

  return <MenuBase title="Seleccione una opcion" options={options} />;
}

export default MenuCliente;
