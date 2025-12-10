import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-proveedor-dashboard',
  standalone: true,
  imports: [CommonModule],
  template: `
  <div class="font-display bg-background-light dark:bg-background-dark text-[#18181B] dark:text-gray-200 min-h-screen">
    <div class="relative flex h-auto min-h-screen w-full flex-col bg-background-light dark:bg-background-dark overflow-x-hidden">
      <div class="flex min-h-screen">
        <aside class="flex w-64 flex-col gap-y-4 border-r border-slate-200 dark:border-slate-800 bg-white dark:bg-slate-900 p-4">
          <div class="flex items-center gap-4 text-slate-900 dark:text-white px-3 py-2">
            <span class="material-symbols-outlined text-3xl text-primary">hub</span>
            <h2 class="text-lg font-bold tracking-[-0.015em]">EvenPro</h2>
          </div>
          <div class="flex flex-col justify-between grow">
            <div class="flex flex-col gap-2 mt-4">
              <div class="flex items-center gap-3 px-3 py-2 rounded-lg bg-primary/20">
                <span class="material-symbols-outlined text-slate-900 dark:text-white">dashboard</span>
                <p class="text-slate-900 dark:text-white text-sm font-medium leading-normal">Dashboard</p>
              </div>
              <div class="flex items-center gap-3 px-3 py-2 rounded-lg hover:bg-primary/10 cursor-pointer">
                <span class="material-symbols-outlined text-slate-600 dark:text-slate-300">inbox</span>
                <p class="text-slate-600 dark:text-slate-300 text-sm font-medium leading-normal">Solicitudes</p>
              </div>
              <div class="flex items-center gap-3 px-3 py-2 rounded-lg hover:bg-primary/10 cursor-pointer">
                <span class="material-symbols-outlined text-slate-600 dark:text-slate-300">calendar_today</span>
                <p class="text-slate-600 dark:text-slate-300 text-sm font-medium leading-normal">Calendario</p>
              </div>
              <div class="flex items-center gap-3 px-3 py-2 rounded-lg hover:bg-primary/10 cursor-pointer">
                <span class="material-symbols-outlined text-slate-600 dark:text-slate-300">mail</span>
                <p class="text-slate-600 dark:text-slate-300 text-sm font-medium leading-normal">Mensajes</p>
              </div>
              <div class="flex items-center gap-3 px-3 py-2 rounded-lg hover:bg-primary/10 cursor-pointer">
                <span class="material-symbols-outlined text-slate-600 dark:text-slate-300">person</span>
                <p class="text-slate-600 dark:text-slate-300 text-sm font-medium leading-normal">Perfil</p>
              </div>
            </div>
            <div class="flex flex-col gap-4">
              <div class="flex gap-3 items-center">
                <div class="bg-center bg-no-repeat aspect-square bg-cover rounded-full size-10" style="background-image: url('https://lh3.googleusercontent.com/aida-public/AB6AXuAYQ3Xv_YuY339LzWOL4jYfKwpp_Xk9EQPeGlaPTZUaCbWibigjj_YB-aGxvhwg8F6DZMvP78IzouOQH3-QD04rKwZu0qAV4ksMNwLhpVskYFEt4FmVucm_mFLxLxPTX8hDHUjR_Z9oMgFc_G87oiDiH7JpnVMSiQivqyiCyL3FHFneBsNk31-5d9q8uvRmqI_l6FgX35MdysNRvagVfmucr0CWN1v_HLjU_aiWNcTSeh51R5rwoZnxazDlwLlmCDHhNO9UufJdhm1M');"></div>
                <div class="flex flex-col">
                  <h1 class="text-slate-900 dark:text-white text-base font-medium leading-normal">Andrés García</h1>
                  <p class="text-primary/80 dark:text-primary/70 text-sm font-normal leading-normal">Proveedor Verificado</p>
                </div>
              </div>
            </div>
          </div>
        </aside>

        <main class="flex-1">
          <header class="flex items-center justify-between whitespace-nowrap border-b border-solid border-slate-200 dark:border-slate-800 px-10 py-3 bg-white dark:bg-slate-900">
            <div class="flex items-center gap-8">
              <label class="relative flex flex-col min-w-40 !h-10 max-w-64">
                <div class="absolute inset-y-0 left-0 flex items-center pl-4 text-slate-500 dark:text-slate-400">
                  <span class="material-symbols-outlined">search</span>
                </div>
                <input class="form-input flex w-full min-w-0 flex-1 resize-none overflow-hidden rounded-lg text-slate-900 dark:text-white focus:outline-0 focus:ring-0 border-none bg-background-light dark:bg-background-dark focus:border-none h-full placeholder:text-slate-500 dark:placeholder:text-slate-400 pl-12 text-base font-normal leading-normal" placeholder="Search..." />
              </label>
            </div>
            <div class="flex flex-1 justify-end gap-4 items-center">
              <button class="flex cursor-pointer items-center justify-center overflow-hidden rounded-lg h-10 w-10 bg-background-light dark:bg-background-dark text-slate-600 dark:text-slate-300 relative">
                <span class="material-symbols-outlined">notifications</span>
                <div class="absolute top-1.5 right-1.5 w-2.5 h-2.5 bg-red-500 rounded-full border-2 border-white dark:border-slate-900"></div>
              </button>
              <div class="bg-center bg-no-repeat aspect-square bg-cover rounded-full size-10" style="background-image: url('https://lh3.googleusercontent.com/aida-public/AB6AXuBcMde1moEravtwv8XA6MOPpaHD_wujI5iVhwujYmnKWi0y51eW-US7EBTEZniLaxw9mmLZqQiYcXk6cYA0VKkOAd-nnbn4OaioFUhPadX-OGcvy6Ijl1tqCV3gmp1qthr_xa2L6HlCkd78w_IkyM1gzDocIbeT5aAwunNgyPXGjNidMKE9ePJTTkODravc8tQ4-mFwCjxgiW7Mq8Nfv4De5YF8thrxFMYFvWhG-ZYmVHPHEJMJ8Mox28ZQ9-BbAf8pzfsYnGQrOenQ');"></div>
            </div>
          </header>

          <div class="p-10">
            <div class="flex flex-wrap justify-between gap-3 items-center">
              <p class="text-slate-900 dark:text-white text-4xl font-black leading-tight tracking-[-0.033em] min-w-72">Dashboard</p>
              <button class="bg-primary text-white font-bold py-2.5 px-6 rounded-lg flex items-center gap-2">
                <span class="material-symbols-outlined">add_circle</span>
                Crear Nueva Oferta
              </button>
            </div>

            <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6 mt-8">
              <div class="flex flex-col gap-2 rounded-xl p-6 bg-white dark:bg-slate-900 border border-slate-200 dark:border-slate-800">
                <p class="text-slate-600 dark:text-slate-300 text-base font-medium leading-normal">Solicitudes Pendientes</p>
                <p class="text-slate-900 dark:text-white tracking-light text-3xl font-bold leading-tight">12</p>
              </div>
              <div class="flex flex-col gap-2 rounded-xl p-6 bg-white dark:bg-slate-900 border border-slate-200 dark:border-slate-800">
                <p class="text-slate-600 dark:text-slate-300 text-base font-medium leading-normal">Reservas Confirmadas</p>
                <p class="text-slate-900 dark:text-white tracking-light text-3xl font-bold leading-tight">8</p>
              </div>
              <div class="flex flex-col gap-2 rounded-xl p-6 bg-white dark:bg-slate-900 border border-slate-200 dark:border-slate-800">
                <p class="text-slate-600 dark:text-slate-300 text-base font-medium leading-normal">Mensajes Sin Leer</p>
                <p class="text-slate-900 dark:text-white tracking-light text-3xl font-bold leading-tight">5</p>
              </div>
              <div class="flex flex-col gap-2 rounded-xl p-6 bg-white dark:bg-slate-900 border border-slate-200 dark:border-slate-800">
                <p class="text-slate-600 dark:text-slate-300 text-base font-medium leading-normal">Ingresos del Mes</p>
                <p class="text-slate-900 dark:text-white tracking-light text-3xl font-bold leading-tight">$8,500</p>
              </div>
            </div>

            <div class="mt-8 bg-white dark:bg-slate-900 rounded-xl border border-slate-200 dark:border-slate-800">
              <div class="pb-3 pt-2">
                <div class="flex border-b border-slate-200 dark:border-slate-800 px-6 gap-8">
                  <a class="flex flex-col items-center justify-center border-b-[3px] border-b-primary text-slate-900 dark:text-white pb-[13px] pt-4" href="#">
                    <p class="text-sm font-bold leading-normal tracking-[0.015em]">Nuevas Solicitudes</p>
                  </a>
                  <a class="flex flex-col items-center justify-center border-b-[3px] border-b-transparent text-slate-500 dark:text-slate-400 pb-[13px] pt-4" href="#">
                    <p class="text-sm font-bold leading-normal tracking-[0.015em]">En Negociación</p>
                  </a>
                  <a class="flex flex-col items-center justify-center border-b-[3px] border-b-transparent text-slate-500 dark:text-slate-400 pb-[13px] pt-4" href="#">
                    <p class="text-sm font-bold leading-normal tracking-[0.015em]">Historial</p>
                  </a>
                </div>
              </div>

              <div class="overflow-x-auto">
                <table class="w-full text-left">
                  <thead class="border-b border-slate-200 dark:border-slate-800">
                    <tr>
                      <th class="p-6 text-sm font-semibold text-slate-500 dark:text-slate-400">Cliente</th>
                      <th class="p-6 text-sm font-semibold text-slate-500 dark:text-slate-400">Tipo de Evento</th>
                      <th class="p-6 text-sm font-semibold text-slate-500 dark:text-slate-400">Fecha</th>
                      <th class="p-6 text-sm font-semibold text-slate-500 dark:text-slate-400">Estado</th>
                      <th class="p-6 text-sm font-semibold text-slate-500 dark:text-slate-400 text-right">Acciones</th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr class="border-b border-slate-200 dark:border-slate-800 hover:bg-slate-50 dark:hover:bg-slate-800/50">
                      <td class="p-6 text-sm font-medium text-slate-800 dark:text-slate-100">Laura Martinez</td>
                      <td class="p-6 text-sm text-slate-600 dark:text-slate-300">Boda</td>
                      <td class="p-6 text-sm text-slate-600 dark:text-slate-300">25/12/2024</td>
                      <td class="p-6 text-sm">
                        <span class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-orange-100 text-orange-800">Pendiente</span>
                      </td>
                      <td class="p-6 text-right space-x-2">
                        <button class="text-slate-600 dark:text-slate-300 hover:text-primary dark:hover:text-primary text-sm font-bold py-2 px-4 rounded-lg border border-slate-300 dark:border-slate-700">Ver Detalle</button>
                        <button class="bg-primary/90 hover:bg-primary text-white text-sm font-bold py-2 px-4 rounded-lg">Responder</button>
                      </td>
                    </tr>
                    <tr class="border-b border-slate-200 dark:border-slate-800 hover:bg-slate-50 dark:hover:bg-slate-800/50">
                      <td class="p-6 text-sm font-medium text-slate-800 dark:text-slate-100">Carlos Rivera</td>
                      <td class="p-6 text-sm text-slate-600 dark:text-slate-300">Conferencia Corporativa</td>
                      <td class="p-6 text-sm text-slate-600 dark:text-slate-300">15/01/2025</td>
                      <td class="p-6 text-sm">
                        <span class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-orange-100 text-orange-800">Pendiente</span>
                      </td>
                      <td class="p-6 text-right space-x-2">
                        <button class="text-slate-600 dark:text-slate-300 hover:text-primary dark:hover:text-primary text-sm font-bold py-2 px-4 rounded-lg border border-slate-300 dark:border-slate-700">Ver Detalle</button>
                        <button class="bg-primary/90 hover:bg-primary text-white text-sm font-bold py-2 px-4 rounded-lg">Responder</button>
                      </td>
                    </tr>
                    <tr class="hover:bg-slate-50 dark:hover:bg-slate-800/50">
                      <td class="p-6 text-sm font-medium text-slate-800 dark:text-slate-100">Sofía Gómez</td>
                      <td class="p-6 text-sm text-slate-600 dark:text-slate-300">Cumpleaños</td>
                      <td class="p-6 text-sm text-slate-600 dark:text-slate-300">10/02/2025</td>
                      <td class="p-6 text-sm">
                        <span class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-blue-100 text-blue-800">En Negociación</span>
                      </td>
                      <td class="p-6 text-right space-x-2">
                        <button class="text-slate-600 dark:text-slate-300 hover:text-primary dark:hover:text-primary text-sm font-bold py-2 px-4 rounded-lg border border-slate-300 dark:border-slate-700">Ver Detalle</button>
                        <button class="bg-primary/90 hover:bg-primary text-white text-sm font-bold py-2 px-4 rounded-lg">Responder</button>
                      </td>
                    </tr>
                  </tbody>
                </table>
              </div>
            </div>
          </div>
        </main>
      </div>
    </div>
  </div>
  `
})
export class ProveedorDashboardComponent {}
