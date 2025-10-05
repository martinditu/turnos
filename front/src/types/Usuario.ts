export interface Usuario {
  email: string;
  token: string;
  rol: "ADMIN" | "CLIENTE";
}

