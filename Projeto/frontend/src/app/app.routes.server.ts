import { RenderMode, ServerRoute } from '@angular/ssr';

export const serverRoutes: ServerRoute[] = [
  // Area logada (painel): depende de localStorage (login) e de dados que so
  // existem em tempo real no json-server, alem de ter rota com parametro
  // (pets/:id) sem getPrerenderParams. Prerenderizar isso no build falhava
  // ou gerava paginas que nunca terminavam de carregar. Renderiza so no
  // navegador, igual o authenticationGuard ja espera.
  {
    path: 'painel',
    renderMode: RenderMode.Client,
  },
  {
    path: 'painel/**',
    renderMode: RenderMode.Client,
  },

  // Paginas publicas e estaticas continuam pre-renderizadas normalmente.
  {
    path: '**',
    renderMode: RenderMode.Prerender,
  },
];
