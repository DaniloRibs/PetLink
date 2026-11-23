const API_URL = process.env.API_URL || "http://localhost:8080/api";

export async function deleteUserByEmail(email: string): Promise<void> {
  try {
    const response = await fetch(`${API_URL}/user/email/${encodeURIComponent(email)}`, { signal: AbortSignal.timeout(5000) });
    if (!response.ok) {
      return;
    }
    const user = (await response.json()) as { id?: number };
    if (user.id) {
      await fetch(`${API_URL}/user/${user.id}`, { method: "DELETE", signal: AbortSignal.timeout(5000) });
    }
  } catch {
    
  }
}
