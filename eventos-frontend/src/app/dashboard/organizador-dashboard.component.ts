import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-organizador-dashboard',
  standalone: true,
  imports: [CommonModule],
  template: `
  <div class="font-display bg-background-light dark:bg-background-dark text-[#18181B] dark:text-gray-200 min-h-screen">
    <div class="relative flex min-h-screen w-full">
      <aside class="flex h-screen w-64 flex-col border-r border-gray-200 dark:border-gray-800 bg-white dark:bg-background-dark sticky top-0">
        <div class="flex h-full flex-col justify-between p-4">
          <div class="flex flex-col gap-4">
            <div class="flex items-center gap-3 px-3 py-2">
              <div class="bg-center bg-no-repeat aspect-square bg-cover rounded-full size-10" style="background-image: url('https://lh3.googleusercontent.com/aida-public/AB6AXuD_N3byYGxORJKe6osQbW1Xxw6vzhzXjs3TJx8tRXVmZdjYqe2fItmel82Up-7FUXsIfbFBW7meG0OGKfttIlkFtIiRiopVGuYyfVPoKU8wX6h_k_U2A39Ml82ZoCRwugKxQ1JuutjFQ-dh6AH1-ilk3IRGng6Hf2vIm3hBctSmTeH9s6E1fekH7Vc428WIa9i3qy0n0fS22j_ZZ0CTVtpLU8A6qxaamZj8hQM_59CbvGMerIIoBSO0rhXTXVaXoG7UF1X0Hc4qUGJw');"></div>
              <div class="flex flex-col">
                <h1 class="text-base font-bold text-gray-900 dark:text-white">EventPro</h1>
                <p class="text-sm text-gray-500 dark:text-gray-400">Workspace</p>
              </div>
            </div>
            <nav class="flex flex-col gap-2 mt-4">
              <a class="flex items-center gap-3 px-3 py-2 rounded-lg bg-primary/20 text-primary" href="#">
                <span class="material-symbols-outlined">dashboard</span>
                <p class="text-sm font-medium">Dashboard</p>
              </a>
              <a class="flex items-center gap-3 px-3 py-2 text-gray-600 dark:text-gray-300 hover:bg-gray-100 dark:hover:bg-gray-800 rounded-lg" href="#">
                <span class="material-symbols-outlined">calendar_month</span>
                <p class="text-sm font-medium">Mis Eventos</p>
              </a>
              <a class="flex items-center gap-3 px-3 py-2 text-gray-600 dark:text-gray-300 hover:bg-gray-100 dark:hover:bg-gray-800 rounded-lg" href="#">
                <span class="material-symbols-outlined">mail</span>
                <p class="text-sm font-medium">Mensajes</p>
              </a>
              <a class="flex items-center gap-3 px-3 py-2 text-gray-600 dark:text-gray-300 hover:bg-gray-100 dark:hover:bg-gray-800 rounded-lg" href="#">
                <span class="material-symbols-outlined">settings</span>
                <p class="text-sm font-medium">Ajustes</p>
              </a>
            </nav>
          </div>
          <div class="flex flex-col gap-1">
            <a class="flex items-center gap-3 px-3 py-2 text-gray-600 dark:text-gray-300 hover:bg-gray-100 dark:hover:bg-gray-800 rounded-lg" href="#">
              <span class="material-symbols-outlined">logout</span>
              <p class="text-sm font-medium">Cerrar Sesión</p>
            </a>
          </div>
        </div>
      </aside>

      <main class="flex-1 flex flex-col">
        <header class="flex h-16 items-center justify-end gap-4 border-b border-gray-200 dark:border-gray-800 bg-white dark:bg-background-dark px-6 sticky top-0 z-10">
          <button class="relative p-2 text-gray-600 dark:text-gray-300 hover:text-gray-900 dark:hover:text-white">
            <span class="material-symbols-outlined">notifications</span>
            <span class="absolute top-2 right-2 flex h-2 w-2">
              <span class="animate-ping absolute inline-flex h-full w-full rounded-full bg-primary opacity-75"></span>
              <span class="relative inline-flex rounded-full h-2 w-2 bg-primary"></span>
            </span>
          </button>
        </header>

        <div class="flex-1 overflow-y-auto p-6 md:p-8 lg:p-10">
          <div class="mx-auto max-w-7xl">
            <div class="flex flex-wrap items-center justify-between gap-4 mb-8">
              <h1 class="text-3xl font-bold tracking-tight text-gray-900 dark:text-white">¡Bienvenido de nuevo, Carlos!</h1>
              <button class="flex min-w-[84px] items-center justify-center gap-2 overflow-hidden rounded-lg h-10 px-5 bg-primary text-white text-sm font-bold shadow-sm hover:bg-primary/90 focus:outline-none focus:ring-2 focus:ring-primary focus:ring-offset-2 dark:focus:ring-offset-background-dark">
                <span class="material-symbols-outlined !text-xl">search</span>
                <span>Buscar Ofertas</span>
              </button>
            </div>

            <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6 mb-8">
              <div class="flex flex-col gap-2 rounded-xl p-6 border border-gray-200 dark:border-gray-800 bg-white dark:bg-gray-900/50">
                <p class="text-base font-medium text-gray-500 dark:text-gray-400">Solicitudes Enviadas</p>
                <p class="text-3xl font-bold text-gray-900 dark:text-white">12</p>
              </div>
              <div class="flex flex-col gap-2 rounded-xl p-6 border border-gray-200 dark:border-gray-800 bg-white dark:bg-gray-900/50">
                <p class="text-base font-medium text-gray-500 dark:text-gray-400">Reservas Confirmadas</p>
                <p class="text-3xl font-bold text-gray-900 dark:text-white">3</p>
              </div>
              <div class="flex flex-col gap-2 rounded-xl p-6 border border-gray-200 dark:border-gray-800 bg-white dark:bg-gray-900/50">
                <p class="text-base font-medium text-gray-500 dark:text-gray-400">Acciones Pendientes</p>
                <p class="text-3xl font-bold text-gray-900 dark:text-white">2</p>
              </div>
            </div>

            <div class="grid grid-cols-1 lg:grid-cols-3 gap-8">
              <div class="lg:col-span-2">
                <div class="rounded-xl border border-gray-200 dark:border-gray-800 bg-white dark:bg-gray-900/50">
                  <div class="px-6 py-4 border-b border-gray-200 dark:border-gray-800">
                    <h2 class="text-lg font-bold text-gray-900 dark:text-white">Solicitudes Recientes</h2>
                  </div>
                  <div class="p-2 sm:p-4">
                    <div class="flow-root">
                      <div class="divide-y divide-gray-200 dark:divide-gray-800">
                        <div class="flex flex-wrap items-center justify-between gap-4 p-4 hover:bg-gray-50 dark:hover:bg-gray-800/50 rounded-lg">
                          <div class="flex items-center gap-4">
                            <div class="bg-center bg-no-repeat aspect-square bg-cover rounded-lg size-12" style="background-image: url('https://lh3.googleusercontent.com/aida-public/AB6AXuD2RbDiDkmC5PrxAAoXWYdtHD3dC5Y5DoWLOLao2YGrDWR7kKvDCzr0YQB0HyryQxoI32YmraRNdYUDzqMQljmwkuwWWUZiPPkW--Qh6Kcalvm44elEfnCuVsQgG6lSXuePhGRVZm9WNg2O4-8Ed-ySn3_QvK7TJs_mYzTsDnEkdA45hbcUl2Tq9LHmuDm0CAqeathDk936rV2KD4iLKIPdTIPwOJhAQxfHENbyE8gfkEirhhazGYoLou3p5H_tSCYaqpZMYUqXW36o');"></div>
                            <div>
                              <p class="font-semibold text-gray-800 dark:text-gray-100">Hacienda La Gavia</p>
                              <p class="text-sm text-gray-500 dark:text-gray-400">Espacio · 12 de Oct, 2024</p>
                            </div>
                          </div>
                          <div class="flex items-center gap-2 text-sm font-medium text-yellow-600 dark:text-yellow-400 bg-yellow-100 dark:bg-yellow-900/50 px-3 py-1 rounded-full">
                            <span class="material-symbols-outlined !text-base">hourglass_top</span>
                            <span>Pendiente</span>
                          </div>
                        </div>
                        <div class="flex flex-wrap items-center justify-between gap-4 p-4 hover:bg-gray-50 dark:hover:bg-gray-800/50 rounded-lg">
                          <div class="flex items-center gap-4">
                            <div class="bg-center bg-no-repeat aspect-square bg-cover rounded-lg size-12" style="background-image: url('https://lh3.googleusercontent.com/aida-public/AB6AXuAZWiLxDtMaCE9K-z9AzP1jn7pM37RxGu4umYgk2vzMR1dxfl8XKkQFFuFc3BWvDcG2DIlVSKrHa78_nkJqGr4a8Mo2HDUzIUVbVO09PnJ_BjlNmjg4SltUvvHrH7WW4fko6uzt5qDOUTTYztMZa_MQFOxiAnIrkM9gAl84I8OTzbBRL-GhSuXe68b6JpSXl0CBJ-RwWX-ytENLl6DdGFya26T8nBLGMaFlS-xwkeOQ4zSEyY69d5uUWzuofdsSbwsxDhYaUzprXO98');"></div>
                            <div>
                              <p class="font-semibold text-gray-800 dark:text-gray-100">Catering Gourmet Delights</p>
                              <p class="text-sm text-gray-500 dark:text-gray-400">Catering · 12 de Oct, 2024</p>
                            </div>
                          </div>
                          <div class="flex items-center gap-2 text-sm font-medium text-green-600 dark:text-green-400 bg-green-100 dark:bg-green-900/50 px-3 py-1 rounded-full">
                            <span class="material-symbols-outlined !text-base">check_circle</span>
                            <span>Aprobado</span>
                          </div>
                        </div>
                        <div class="flex flex-wrap items-center justify-between gap-4 p-4 hover:bg-gray-50 dark:hover:bg-gray-800/50 rounded-lg">
                          <div class="flex items-center gap-4">
                            <div class="bg-center bg-no-repeat aspect-square bg-cover rounded-lg size-12" style="background-image: url('https://lh3.googleusercontent.com/aida-public/AB6AXuBDTb8Xfpc-QV8gmNXDUUhF0gicFFNELZUL1n6b1aRNgiIE59ynearLe9cvFCKUhackegr1bWqJGCx-CruIxWDPJ0Cxpn2b0mCNZL5_Rl8KyHZG7h-n5b9uCX367aZgo6FDRnqj1GATrBR-HRQauQJAv9Wr4Iez2RNtPX_1tCNG-8X-EFcUnvmwX4nQxyqxWzNBNDLAv3vF94JosNe6wKfKttSSwhNGvhrH4Ngu6ZqLS_dXaDKDkh5EVY4yc6Wf-j_gAfT4NXxVJAwd');"></div>
                            <div>
                              <p class="font-semibold text-gray-800 dark:text-gray-100">Soundwave Productions</p>
                              <p class="text-sm text-gray-500 dark:text-gray-400">Audiovisual · 22 de Sep, 2024</p>
                            </div>
                          </div>
                          <div class="flex items-center gap-2 text-sm font-medium text-red-600 dark:text-red-400 bg-red-100 dark:bg-red-900/50 px-3 py-1 rounded-full">
                            <span class="material-symbols-outlined !text-base">cancel</span>
                            <span>Rechazado</span>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>

              <div class="lg:col-span-1">
                <div class="rounded-xl border border-gray-200 dark:border-gray-800 bg-white dark:bg-gray-900/50">
                  <div class="px-6 py-4 border-b border-gray-200 dark:border-gray-800">
                    <h2 class="text-lg font-bold text-gray-900 dark:text-white">Próximas Reservas</h2>
                  </div>
                  <div class="p-4">
                    <div class="flex flex-col gap-4">
                      <div class="flex items-start gap-4 p-3 hover:bg-gray-50 dark:hover:bg-gray-800/50 rounded-lg">
                        <div class="flex flex-col items-center justify-center p-3 w-16 h-16 bg-primary/20 text-primary rounded-lg">
                          <span class="text-sm font-bold uppercase">AGO</span>
                          <span class="text-2xl font-black">28</span>
                        </div>
                        <div>
                          <p class="font-semibold text-gray-800 dark:text-gray-100">Lanzamiento de Producto Tech</p>
                          <p class="text-sm text-gray-500 dark:text-gray-400">Centro de Convenciones</p>
                        </div>
                      </div>
                      <div class="flex items-start gap-4 p-3 hover:bg-gray-50 dark:hover:bg-gray-800/50 rounded-lg">
                        <div class="flex flex-col items-center justify-center p-3 w-16 h-16 bg-primary/20 text-primary rounded-lg">
                          <span class="text-sm font-bold uppercase">SEP</span>
                          <span class="text-2xl font-black">15</span>
                        </div>
                        <div>
                          <p class="font-semibold text-gray-800 dark:text-gray-100">Boda de Ana y Juan</p>
                          <p class="text-sm text-gray-500 dark:text-gray-400">Jardín Botánico</p>
                        </div>
                      </div>
                      <div class="flex items-start gap-4 p-3 hover:bg-gray-50 dark:hover:bg-gray-800/50 rounded-lg">
                        <div class="flex flex-col items-center justify-center p-3 w-16 h-16 bg-primary/20 text-primary rounded-lg">
                          <span class="text-sm font-bold uppercase">OCT</span>
                          <span class="text-2xl font-black">12</span>
                        </div>
                        <div>
                          <p class="font-semibold text-gray-800 dark:text-gray-100">Gala Anual de Caridad</p>
                          <p class="text-sm text-gray-500 dark:text-gray-400">Museo de Arte Moderno</p>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>

          </div>
        </div>
      </main>
    </div>
  </div>
  `
})
export class OrganizadorDashboardComponent {}
