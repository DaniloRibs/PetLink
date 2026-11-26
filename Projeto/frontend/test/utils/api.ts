import users from "../data/users";

const API_URL = process.env.API_URL || "http://localhost:8080/api";
const AUTH_URL = process.env.AUTH_URL || `${API_URL.replace(/\/api$/, "")}/authenticate`;

async function login(email: string, password: string): Promise<string | null> {
  const response = await fetch(AUTH_URL, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ email, password }),
    signal: AbortSignal.timeout(5000),
  });
  if (!response.ok) {
    return null;
  }
  const body = (await response.json()) as { token?: string };
  return body.token ?? null;
}

export async function deleteUserByEmail(email: string, password: string = users.person.password): Promise<void> {
  try {
    const token = await login(email, password);
    if (!token) {
      return;
    }
    const headers = { Authorization: `Bearer ${token}` };

    const response = await fetch(`${API_URL}/user/email/${encodeURIComponent(email)}`, { headers, signal: AbortSignal.timeout(5000) });
    if (!response.ok) {
      return;
    }
    const user = (await response.json()) as { id?: number };
    if (user.id) {
      await fetch(`${API_URL}/user/${user.id}`, { method: "DELETE", headers, signal: AbortSignal.timeout(5000) });
    }
  } catch {
    
  }
}
