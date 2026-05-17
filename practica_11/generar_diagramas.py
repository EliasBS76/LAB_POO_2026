"""
Genera los diagramas UML del manual tecnico de GymManager Pro
como imagenes PNG usando matplotlib.
"""
import matplotlib
matplotlib.use("Agg")
import matplotlib.pyplot as plt
import matplotlib.patches as mpatches
from matplotlib.patches import FancyBboxPatch, FancyArrowPatch
import os

OUT = r"c:\Users\elias\datathon_practica\LAB_POO_2026\practica_11\diagramas"
os.makedirs(OUT, exist_ok=True)

# ─── helpers ──────────────────────────────────────────────────────────────────

FONT = "monospace"
BG   = "#FAFAFA"

def uml_class(ax, x, y, w, h, name, attrs, methods,
              head_color="#2B4C7E", head_text="#FFFFFF",
              body_color="#EEF3FB", border="#2B4C7E"):
    """Draw a UML class box with name, attributes and methods sections."""
    line_h = 0.048
    pad     = 0.012

    total_lines = len(attrs) + len(methods) + (1 if (attrs and methods) else 0)
    h = max(h, line_h * (total_lines + 2) + 0.06)

    # header
    header_h = 0.07
    head = FancyBboxPatch((x, y + h - header_h), w, header_h,
                          boxstyle="square,pad=0", linewidth=1.2,
                          edgecolor=border, facecolor=head_color)
    ax.add_patch(head)
    ax.text(x + w/2, y + h - header_h/2, f"«class»\n{name}",
            ha="center", va="center", fontsize=7.5, fontweight="bold",
            color=head_text, fontfamily=FONT, linespacing=1.3)

    # body
    body = FancyBboxPatch((x, y), w, h - header_h,
                          boxstyle="square,pad=0", linewidth=1.2,
                          edgecolor=border, facecolor=body_color)
    ax.add_patch(body)

    cur_y = y + h - header_h - pad - line_h
    for a in attrs:
        ax.text(x + pad*2, cur_y, a, fontsize=6.5, va="top",
                fontfamily=FONT, color="#1a1a2e")
        cur_y -= line_h

    if attrs and methods:
        ax.plot([x, x+w], [cur_y + line_h*0.5, cur_y + line_h*0.5],
                color=border, linewidth=0.7, alpha=0.5)
        cur_y -= line_h * 0.3

    for m in methods:
        ax.text(x + pad*2, cur_y, m, fontsize=6.5, va="top",
                fontfamily=FONT, color="#1a1a2e", style="italic")
        cur_y -= line_h

    return x + w/2, y + h, x + w/2, y   # top_cx, top_cy, bot_cx, bot_cy


def arrow(ax, x1, y1, x2, y2, style="->", color="#2B4C7E", label=""):
    ax.annotate("", xy=(x2, y2), xytext=(x1, y1),
                arrowprops=dict(arrowstyle=style, color=color,
                                lw=1.2, connectionstyle="arc3,rad=0"))
    if label:
        mx, my = (x1+x2)/2, (y1+y2)/2
        ax.text(mx+0.01, my, label, fontsize=6, color=color, fontfamily=FONT)


def diamond_arrow(ax, x1, y1, x2, y2, color="#2B4C7E", label=""):
    """Association with filled diamond at source."""
    ax.annotate("", xy=(x2, y2), xytext=(x1, y1),
                arrowprops=dict(arrowstyle="-|>",
                                color=color, lw=1.2,
                                connectionstyle="arc3,rad=0"))
    if label:
        mx, my = (x1+x2)/2, (y1+y2)/2
        ax.text(mx+0.01, my+0.01, label, fontsize=6, color=color)


def inheritance_arrow(ax, x1, y1, x2, y2, color="#2B4C7E"):
    """Open triangle for inheritance."""
    ax.annotate("", xy=(x2, y2), xytext=(x1, y1),
                arrowprops=dict(arrowstyle="-|>",
                                color=color, lw=1.3,
                                mutation_scale=14,
                                connectionstyle="arc3,rad=0"))


# ═══════════════════════════════════════════════════════════════════════════════
# DIAGRAMA 1 — Modelo de dominio
# Coordenadas en pulgadas (figsize 16x10 => xlim 0-16, ylim 0-10)
# ═══════════════════════════════════════════════════════════════════════════════

def uml2(ax, x, y, w, name, attrs, methods,
         hc="#2B4C7E", tc="#FFFFFF", bc="#EEF3FB", ec="#2B4C7E",
         fs=8, lh=0.45, hh=0.7):
    """
    Draw UML class box. Coordinates in data units that match figsize.
    x,y = bottom-left corner. w = width.
    lh = line height, hh = header height.
    Returns (box_top_y, box_bottom_y, center_x).
    """
    n_lines = len(attrs) + len(methods) + (1 if attrs and methods else 0)
    body_h  = lh * n_lines + lh * 0.5
    total_h = hh + body_h

    # header
    head = FancyBboxPatch((x, y + body_h), w, hh,
                          boxstyle="square,pad=0", linewidth=1.5,
                          edgecolor=ec, facecolor=hc)
    ax.add_patch(head)
    ax.text(x + w/2, y + body_h + hh/2,
            f"<<class>>\n{name}",
            ha="center", va="center", fontsize=fs, fontweight="bold",
            color=tc, linespacing=1.35)

    # body
    body = FancyBboxPatch((x, y), w, body_h,
                          boxstyle="square,pad=0", linewidth=1.5,
                          edgecolor=ec, facecolor=bc)
    ax.add_patch(body)

    pad_x = 0.15
    cur = y + body_h - lh * 0.3
    for a in attrs:
        cur -= lh
        ax.text(x + pad_x, cur + lh*0.25, a,
                fontsize=fs - 1, va="center", color="#1a1a2e")

    if attrs and methods:
        div_y = cur - lh * 0.1
        ax.plot([x, x+w], [div_y, div_y], color=ec, lw=0.8, alpha=0.4)
        cur -= lh * 0.35

    for m in methods:
        cur -= lh
        ax.text(x + pad_x, cur + lh*0.25, m,
                fontsize=fs - 1, va="center", color="#333", style="italic")

    return y + total_h, y, x + w/2


fig, ax = plt.subplots(figsize=(16, 10))
ax.set_xlim(0, 16)
ax.set_ylim(0, 10)
ax.axis("off")
fig.patch.set_facecolor(BG)
ax.set_facecolor(BG)
ax.set_title("Diagrama de Clases - Modelo de Dominio", fontsize=14,
             fontweight="bold", pad=14, color="#1a1a2e")

W = 3.2   # box width
LH = 0.40 # line height
HH = 0.75 # header height

# ── fila superior: Cliente | Membresia | Plan ──────────────────────────────
top, bot, cx_c = uml2(ax, 0.3, 5.2, W,
    "Cliente",
    ["- id: int", "- nombre: String", "- email: String",
     "- telefono: String", "- puntos: int",
     "- membresias: List<Membresia>"],
    ["+ getMembresiaActiva()", "+ agregarPuntos(int)"],
    lh=LH, hh=HH)

top, bot, cx_m = uml2(ax, 4.2, 5.2, W,
    "Membresia",
    ["- id: int", "- plan: Plan", "- fechaInicio: LocalDate",
     "- fechaFin: LocalDate", "- renovacion: boolean",
     "- estado: EstadoMembresia"],
    ["+ renovar()", "+ estaVencida(): boolean", "+ diasRestantes(): long"],
    lh=LH, hh=HH)

top, bot, cx_p = uml2(ax, 8.1, 5.8, W,
    "Plan  (enum)",
    ["BASICO   : $25  / 10 pts", "ESTANDAR : $45  / 20 pts",
     "PREMIUM  : $75  / 50 pts", "VIP      : $120 / 100 pts"],
    ["+ getPrecioFinal(): double"],
    hc="#5C4B8A", ec="#5C4B8A", bc="#EDE7F6", lh=LH, hh=HH)

# ── fila inferior: Equipo | Pago | RegistroAcceso | ClaseGrupal ────────────
top_e, bot_e, cx_e = uml2(ax, 0.3, 0.5, W,
    "Equipo",
    ["- id: int", "- nombre: String", "- tipo: String",
     "- cantidad: int", "- estado: EstadoEquipo",
     "- ultimoMantenimiento: LocalDate"],
    [],
    hc="#1E8449", ec="#1E8449", bc="#D5F5E3", lh=LH, hh=HH)

top_pa, bot_pa, cx_pa = uml2(ax, 4.2, 0.5, W,
    "Pago",
    ["- id: int", "- cliente: Cliente", "- plan: Plan",
     "- monto: double", "- metodo: String",
     "- estado: EstadoPago"],
    [],
    hc="#784212", ec="#784212", bc="#FDEBD0", lh=LH, hh=HH)

top_r, bot_r, cx_r = uml2(ax, 8.1, 0.5, W,
    "RegistroAcceso",
    ["- id: int", "- cliente: Cliente", "- tipo: TipoAcceso",
     "- fechaHora: LocalDateTime"],
    ["+ esDeHoy(): boolean"],
    hc="#C0392B", ec="#C0392B", bc="#FADBD8", lh=LH, hh=HH)

top_g, bot_g, cx_g = uml2(ax, 12.0, 3.5, W,
    "ClaseGrupal",
    ["- id: int", "- nombre: String", "- instructor: String",
     "- dia: DayOfWeek", "- hora: LocalTime",
     "- capacidadMaxima: int"],
    ["+ inscribir(int): boolean", "+ getLugaresDisponibles(): int"],
    hc="#1A5276", ec="#1A5276", bc="#D6EAF8", lh=LH, hh=HH)

# ── flechas ────────────────────────────────────────────────────────────────
mid_y = 5.2 + LH * 0.5

# Cliente 1 ---> * Membresia
ax.annotate("", xy=(4.2, mid_y + 1.5), xytext=(0.3 + W, mid_y + 1.5),
            arrowprops=dict(arrowstyle="-|>", color="#2B4C7E", lw=1.5))
ax.text((0.3 + W + 4.2) / 2, mid_y + 1.8,
        "1                       *",
        ha="center", fontsize=8, color="#2B4C7E")

# Membresia --uses--> Plan (dashed)
ax.annotate("", xy=(8.1, 6.6), xytext=(4.2 + W, 6.6),
            arrowprops=dict(arrowstyle="->", color="#5C4B8A", lw=1.3,
                            linestyle="dashed"))
ax.text((4.2 + W + 8.1) / 2, 6.85, "usa",
        ha="center", fontsize=8, color="#5C4B8A", style="italic")

# Pago -> Cliente (ref)
ax.annotate("", xy=(0.3 + W/2, 5.2),
            xytext=(4.2 + W/2, top_pa),
            arrowprops=dict(arrowstyle="->", color="#888", lw=1.1))

# RegistroAcceso -> Cliente (ref)
ax.annotate("", xy=(0.3 + W, 5.4),
            xytext=(8.1, top_r),
            arrowprops=dict(arrowstyle="->", color="#888", lw=1.1))

plt.tight_layout(pad=0.8)
plt.savefig(f"{OUT}/diagrama_clases.png", dpi=150, bbox_inches="tight",
            facecolor=BG)
plt.close()
print("1. diagrama_clases.png")


# ═══════════════════════════════════════════════════════════════════════════════
# DIAGRAMA 2 — Jerarquia de excepciones
# ═══════════════════════════════════════════════════════════════════════════════
fig, ax = plt.subplots(figsize=(9, 5))
ax.set_xlim(0, 9); ax.set_ylim(0, 5)
ax.axis("off")
fig.patch.set_facecolor(BG); ax.set_facecolor(BG)
ax.set_title("Jerarquia de Excepciones", fontsize=13,
             fontweight="bold", pad=12, color="#1a1a2e")

def exc_box(ax, x, y, w, h, name, pkg, color="#C0392B", light="#FADBD8"):
    r = FancyBboxPatch((x-w/2, y-h/2), w, h, boxstyle="round,pad=0.05",
                       linewidth=1.5, edgecolor=color, facecolor=light)
    ax.add_patch(r)
    ax.text(x, y+0.12, f"«exception»", fontsize=7, ha="center",
            color=color, style="italic")
    ax.text(x, y-0.12, name, fontsize=9, ha="center", fontweight="bold",
            color="#1a1a2e", fontfamily=FONT)
    ax.text(x, y-0.38, pkg, fontsize=6.5, ha="center", color="#666")

def inh(ax, x1, y1, x2, y2):
    ax.annotate("", xy=(x2, y2), xytext=(x1, y1),
                arrowprops=dict(arrowstyle="-|>", color="#C0392B",
                                lw=1.5, mutation_scale=14))

exc_box(ax, 4.5, 4.2, 2.8, 0.8, "Exception",      "java.lang",
        color="#7F8C8D", light="#F2F3F4")
exc_box(ax, 4.5, 2.8, 2.8, 0.8, "GymException",   "gym.exception")
exc_box(ax, 2.2, 1.2, 2.8, 0.8, "ClienteException",  "gym.exception")
exc_box(ax, 6.8, 1.2, 2.8, 0.8, "MembresiaException","gym.exception")

inh(ax, 4.5, 3.2, 4.5, 3.8)
inh(ax, 2.2, 1.6, 3.8, 2.4)
inh(ax, 6.8, 1.6, 5.2, 2.4)

ax.text(4.5, 0.4,
        "Todas son checked exceptions: el compilador obliga a manejarlas.",
        ha="center", fontsize=8, color="#555", style="italic")

plt.tight_layout(pad=0.5)
plt.savefig(f"{OUT}/diagrama_excepciones.png", dpi=150, bbox_inches="tight",
            facecolor=BG)
plt.close()
print("2. diagrama_excepciones.png")


# ═══════════════════════════════════════════════════════════════════════════════
# DIAGRAMA 3 — Arquitectura MVC
# ═══════════════════════════════════════════════════════════════════════════════
fig, ax = plt.subplots(figsize=(11, 6))
ax.set_xlim(0, 11); ax.set_ylim(0, 6)
ax.axis("off")
fig.patch.set_facecolor(BG); ax.set_facecolor(BG)
ax.set_title("Arquitectura MVC — GymManager Pro", fontsize=13,
             fontweight="bold", pad=12, color="#1a1a2e")

layers = [
    (1, 4.2, 9, 1.1, "VIEW",       "#1A5276", "#D6EAF8",
     "ClienteView  |  MembresiaView  |  EquipoView  |  PagoView  |  AccesoView  |  ReporteView",
     "Construyen la interfaz grafica. No contienen logica de negocio."),
    (1, 2.7, 9, 1.1, "CONTROLLER", "#1E8449", "#D5F5E3",
     "ClienteController  |  MembresiaController  |  EquipoController  |  AccesoController",
     "Validan entradas, coordinan modelo y servicio, lanzan excepciones."),
    (1, 1.2, 9, 1.1, "MODEL + SERVICE", "#6C3483", "#E8DAEF",
     "Cliente  Membresia  Pago  Equipo  |  DataService  ThemeService  NotificacionService",
     "Entidades del dominio y servicios singleton de soporte."),
]

for lx, ly, lw, lh, title, col, light, content, desc in layers:
    r = FancyBboxPatch((lx, ly), lw, lh, boxstyle="round,pad=0.06",
                       linewidth=2, edgecolor=col, facecolor=light)
    ax.add_patch(r)
    ax.text(lx + 0.18, ly + lh/2 + 0.1, title, fontsize=11,
            fontweight="bold", color=col, va="center")
    ax.text(lx + 0.18, ly + lh/2 - 0.18, content, fontsize=7.5,
            color="#1a1a2e", fontfamily=FONT)
    ax.text(lx + 0.18, ly + 0.1, desc, fontsize=7,
            color="#555", style="italic")

# arrows between layers
for ya, yb, label in [(4.2, 3.8, "eventos UI"), (2.7, 2.3, "consulta / modifica")]:
    ax.annotate("", xy=(5.5, yb), xytext=(5.5, ya),
                arrowprops=dict(arrowstyle="<->", color="#333", lw=1.5))
    ax.text(6.1, (ya+yb)/2, label, fontsize=7.5, color="#333", va="center")

plt.tight_layout(pad=0.5)
plt.savefig(f"{OUT}/diagrama_mvc.png", dpi=150, bbox_inches="tight",
            facecolor=BG)
plt.close()
print("3. diagrama_mvc.png")


# ═══════════════════════════════════════════════════════════════════════════════
# DIAGRAMA 3.5 — Diagrama de paquetes
# ═══════════════════════════════════════════════════════════════════════════════
fig, ax = plt.subplots(figsize=(13, 8))
ax.set_xlim(0, 13); ax.set_ylim(0, 8)
ax.axis("off")
fig.patch.set_facecolor(BG); ax.set_facecolor(BG)
ax.set_title("Diagrama de Paquetes - GymManager Pro", fontsize=13,
             fontweight="bold", pad=12, color="#1a1a2e")

def pkg_box(ax, x, y, w, h, name, color, light, items, font_size=7.5):
    # tab
    tab_w = min(len(name) * 0.13 + 0.3, w)
    tab_h = 0.35
    tab = FancyBboxPatch((x, y + h), tab_w, tab_h,
                         boxstyle="square,pad=0", linewidth=1.5,
                         edgecolor=color, facecolor=color)
    ax.add_patch(tab)
    ax.text(x + tab_w/2, y + h + tab_h/2, name,
            ha="center", va="center", fontsize=8.5, fontweight="bold",
            color="white")
    # body
    body = FancyBboxPatch((x, y), w, h,
                          boxstyle="square,pad=0", linewidth=1.5,
                          edgecolor=color, facecolor=light)
    ax.add_patch(body)
    cur_y = y + h - 0.15
    for item in items:
        cur_y -= 0.30
        ax.text(x + 0.15, cur_y, item, fontsize=font_size,
                color="#1a1a2e", fontfamily=FONT, va="top")

# model
pkg_box(ax, 0.2, 4.6, 3.8, 3.0, "model", "#1A5276", "#D6EAF8",
        ["Cliente", "Membresia", "Plan (enum)", "ClaseGrupal",
         "Equipo", "Pago", "RegistroAcceso", "Recompensa",
         "EstadoMembresia / EstadoEquipo / EstadoPago / TipoAcceso"])

# controller
pkg_box(ax, 4.3, 4.6, 3.8, 3.0, "controller", "#1E8449", "#D5F5E3",
        ["ClienteController", "MembresiaController",
         "ClaseGrupalController", "EquipoController",
         "PagoController", "AccesoController"])

# view
pkg_box(ax, 8.4, 4.6, 4.4, 3.0, "view", "#6C3483", "#E8DAEF",
        ["MainView", "ClienteView", "MembresiaView",
         "ClaseGrupalView", "EquipoView",
         "PagoView", "AccesoView", "ReporteView"])

# service
pkg_box(ax, 0.2, 1.0, 3.8, 3.2, "service", "#784212", "#FDEBD0",
        ["DataService (Singleton)", "NotificacionService (Singleton)",
         "ThemeService (Singleton)", "BackupService (Task<String>)",
         "PagoService", "ReporteService"])

# exception
pkg_box(ax, 4.3, 2.2, 2.4, 2.0, "exception", "#C0392B", "#FADBD8",
        ["GymException", "ClienteException", "MembresiaException"])

# components + dialog + util
pkg_box(ax, 7.0, 2.2, 2.0, 2.0, "components\n+ dialog", "#5D6D7E", "#EAECEE",
        ["SearchableTable<T>", "StatusBadge", "ConfirmDialog"], font_size=7)

pkg_box(ax, 9.3, 2.2, 3.5, 2.0, "util", "#5D6D7E", "#EAECEE",
        ["Serializer", "AlertUtil"])

# Main / GymApp at top center
pkg_box(ax, 5.0, 7.2, 3.0, 0.55, "gym (raiz)", "#2C3E50", "#D5D8DC",
        ["Main.java   GymApp.java"], font_size=7.5)

# dependency arrows (dashed)
def dep(ax, x1, y1, x2, y2):
    ax.annotate("", xy=(x2, y2), xytext=(x1, y1),
                arrowprops=dict(arrowstyle="->", color="#888",
                                lw=1.1, linestyle="dashed"))

# view -> controller
dep(ax, 8.4, 6.1, 8.1, 6.1)
# controller -> model
dep(ax, 4.3, 6.1, 4.0, 6.1)
# controller -> service
dep(ax, 6.15, 4.6, 3.5, 4.2)
# view -> service (ThemeService)
dep(ax, 10.6, 4.6, 3.5, 3.8)

ax.text(6.25, 6.2, "usa", fontsize=7, color="#888", ha="center")
ax.text(4.85, 6.2, "modifica", fontsize=7, color="#888", ha="center")

plt.tight_layout(pad=0.5)
plt.savefig(f"{OUT}/diagrama_paquetes.png", dpi=150, bbox_inches="tight",
            facecolor=BG)
plt.close()
print("3b. diagrama_paquetes.png")


# ═══════════════════════════════════════════════════════════════════════════════
# DIAGRAMA 4 — Diagrama de servicios (Singletons)
# ═══════════════════════════════════════════════════════════════════════════════
fig, ax = plt.subplots(figsize=(13, 5.5))
ax.set_xlim(0, 13); ax.set_ylim(0, 5.5)
ax.axis("off")
fig.patch.set_facecolor(BG); ax.set_facecolor(BG)
ax.set_title("Capa de Servicios", fontsize=13,
             fontweight="bold", pad=12, color="#1a1a2e")

services = [
    (0.3,  1.5, "DataService",          "#154360", "#D6EAF8",
     ["- clientes: List<Cliente>",
      "- clases: List<ClaseGrupal>",
      "- equipos: List<Equipo>",
      "- pagos: List<Pago>",
      "- accesos: List<RegistroAcceso>"],
     ["+ cargarDatos()",
      "+ guardarDatos()",
      "+ clientesEnGimnasio(): long",
      "+ getInstance(): DataService"]),
    (3.6,  2.2, "NotificacionService",  "#145A32", "#D5F5E3",
     ["- notificaciones: List<String>"],
     ["+ verificarVencimientos()",
      "+ getNotificaciones()",
      "+ getInstance()"]),
    (6.9,  2.2, "ThemeService",         "#6C3483", "#E8DAEF",
     ["- dark: boolean",
      "- scenes: List<Scene>"],
     ["+ registerScene(Scene)",
      "+ toggle()",
      "+ isDark(): boolean",
      "+ getInstance()"]),
    (10.2, 2.2, "BackupService",        "#784212", "#FDEBD0",
     ["extends Task<String>"],
     ["+ call(): String",
      "  updateProgress()",
      "  updateMessage()",
      "  serializar snapshot"]),
]

for sx, sy, name, col, light, attrs, methods in services:
    w = 2.8
    total_h = max(0.6 + len(attrs)*0.32 + 0.08 + len(methods)*0.32, 2.0)
    # header
    hh = 0.55
    r_head = FancyBboxPatch((sx, sy+total_h-hh), w, hh,
                            boxstyle="square,pad=0", linewidth=1.5,
                            edgecolor=col, facecolor=col)
    ax.add_patch(r_head)
    ax.text(sx+w/2, sy+total_h-hh/2, f"<<Singleton>>\n{name}",
            ha="center", va="center", fontsize=7.5, fontweight="bold",
            color="white", linespacing=1.4)
    # body
    r_body = FancyBboxPatch((sx, sy), w, total_h-hh,
                            boxstyle="square,pad=0", linewidth=1.5,
                            edgecolor=col, facecolor=light)
    ax.add_patch(r_body)
    cur = sy + total_h - hh - 0.08
    for a in attrs:
        cur -= 0.30
        ax.text(sx+0.1, cur, a, fontsize=6.5, color="#1a1a2e", fontfamily=FONT)
    ax.plot([sx, sx+w], [cur-0.05, cur-0.05], color=col, lw=0.7, alpha=0.5)
    cur -= 0.1
    for m in methods:
        cur -= 0.30
        ax.text(sx+0.1, cur, m, fontsize=6.5, color="#333",
                fontfamily=FONT, style="italic")

# singleton label
for sx, sy, name, *_ in services:
    w = 2.8
    ax.text(sx+w/2, sy-0.3, "getInstance()", fontsize=7,
            ha="center", color="#888", style="italic")

plt.tight_layout(pad=0.5)
plt.savefig(f"{OUT}/diagrama_servicios.png", dpi=150, bbox_inches="tight",
            facecolor=BG)
plt.close()
print("4. diagrama_servicios.png")


# ═══════════════════════════════════════════════════════════════════════════════
# DIAGRAMA 5 — Secuencia: Registro de acceso
# ═══════════════════════════════════════════════════════════════════════════════
fig, ax = plt.subplots(figsize=(12, 7))
ax.set_xlim(0, 12); ax.set_ylim(0, 7)
ax.axis("off")
fig.patch.set_facecolor(BG); ax.set_facecolor(BG)
ax.set_title("Diagrama de Secuencia — Registro de Acceso", fontsize=13,
             fontweight="bold", pad=12, color="#1a1a2e")

actors = [
    (2.0,  "AccesoView",        "#1A5276", "#D6EAF8"),
    (6.0,  "AccesoController",  "#1E8449", "#D5F5E3"),
    (10.0, "DataService",       "#6C3483", "#E8DAEF"),
]

# actor boxes + lifelines
for ax_x, name, col, light in actors:
    r = FancyBboxPatch((ax_x-1.1, 6.2), 2.2, 0.55,
                       boxstyle="round,pad=0.05",
                       linewidth=1.5, edgecolor=col, facecolor=light)
    ax.add_patch(r)
    ax.text(ax_x, 6.475, name, ha="center", va="center",
            fontsize=8, fontweight="bold", color=col)
    ax.plot([ax_x, ax_x], [6.2, 0.2], color=col, lw=1, linestyle="dashed",
            alpha=0.5)

def seq_arrow(ax, x1, x2, y, label, ret=False, col="#333", note=""):
    style = "<-" if ret else "->"
    ax.annotate("", xy=(x2, y), xytext=(x1, y),
                arrowprops=dict(arrowstyle=style, color=col, lw=1.3))
    mid = (x1+x2)/2
    ax.text(mid, y+0.12, label, ha="center", fontsize=7.5,
            color=col, fontweight="bold" if not ret else "normal")
    if note:
        ax.text(mid, y-0.15, note, ha="center", fontsize=6.5,
                color="#777", style="italic")

def alt_box(ax, y_top, y_bot, label, col="#E67E22", light="#FDEBD0"):
    r = FancyBboxPatch((0.3, y_bot), 11.4, y_top-y_bot,
                       boxstyle="square,pad=0", linewidth=1,
                       edgecolor=col, facecolor=light, alpha=0.35)
    ax.add_patch(r)
    ax.text(0.5, y_top-0.1, f"[{label}]", fontsize=7,
            color=col, fontweight="bold")

seq_arrow(ax, 2.0, 6.0, 5.8, "registrar(cliente, ENTRADA)")
seq_arrow(ax, 6.0, 10.0, 5.3, "getAccesos()")
seq_arrow(ax, 10.0, 6.0, 4.9, "List<RegistroAcceso>", ret=True)
ax.text(6.1, 4.55, "filtrar accesos del dia  |  ordenar por hora",
        fontsize=7, color="#555", style="italic")

alt_box(ax, 4.3, 1.5, "ENTRADA", "#1A5276", "#D6EAF8")
seq_arrow(ax, 6.0, 6.0, 4.0, "validar membresia activa",
          col="#1A5276",
          note="si nula -> GymException: 'Sin membresia activa'")
seq_arrow(ax, 6.0, 6.0, 3.35, "validar no doble entrada",
          col="#1A5276",
          note="si ultimo==ENTRADA -> GymException: 'Ya tiene entrada activa'")
seq_arrow(ax, 6.0, 10.0, 2.8, "agregarAcceso(RegistroAcceso)")
seq_arrow(ax, 6.0, 2.0, 2.3, "RegistroAcceso creado", ret=True)
seq_arrow(ax, 2.0, 2.0, 1.8, "mostrar exito en UI", ret=True, col="#1E8449")

plt.tight_layout(pad=0.5)
plt.savefig(f"{OUT}/diagrama_secuencia_acceso.png", dpi=150,
            bbox_inches="tight", facecolor=BG)
plt.close()
print("5. diagrama_secuencia_acceso.png")


# ═══════════════════════════════════════════════════════════════════════════════
# DIAGRAMA 6 — Secuencia: Backup
# ═══════════════════════════════════════════════════════════════════════════════
fig, ax = plt.subplots(figsize=(12, 7))
ax.set_xlim(0, 12); ax.set_ylim(0, 7)
ax.axis("off")
fig.patch.set_facecolor(BG); ax.set_facecolor(BG)
ax.set_title("Diagrama de Secuencia — Backup en Segundo Plano", fontsize=13,
             fontweight="bold", pad=12, color="#1a1a2e")

backup_actors = [
    (1.8,  "MainView",       "#1A5276", "#D6EAF8"),
    (5.5,  "BackupService\n(Task<String>)", "#784212", "#FDEBD0"),
    (10.0, "Serializer",     "#1E8449", "#D5F5E3"),
]
for ax_x, name, col, light in backup_actors:
    r = FancyBboxPatch((ax_x-1.2, 6.2), 2.4, 0.55,
                       boxstyle="round,pad=0.05",
                       linewidth=1.5, edgecolor=col, facecolor=light)
    ax.add_patch(r)
    ax.text(ax_x, 6.475, name, ha="center", va="center",
            fontsize=8, fontweight="bold", color=col, linespacing=1.3)
    ax.plot([ax_x, ax_x], [6.2, 0.2], color=col, lw=1, linestyle="dashed",
            alpha=0.5)

# thread indicator
thread_box = FancyBboxPatch((4.9, 1.2), 1.2, 4.3, boxstyle="square,pad=0",
                             linewidth=1.5, edgecolor="#784212",
                             facecolor="#FDEBD0", alpha=0.5)
ax.add_patch(thread_box)
ax.text(5.5, 0.9, "hilo secundario (daemon)", ha="center", fontsize=7,
        color="#784212", style="italic")

seq_arrow(ax, 1.8, 5.5, 5.8, "new BackupService(dataService)")
seq_arrow(ax, 1.8, 5.5, 5.3, "new Thread(task).start()")
seq_arrow(ax, 5.5, 5.5, 4.8, "updateMessage('Iniciando...')",
          col="#784212", note="updateProgress(0 / 4)")
seq_arrow(ax, 5.5, 5.5, 4.1, "updateMessage('Serializando clientes...')",
          col="#784212", note="updateProgress(1 / 4)")
seq_arrow(ax, 5.5, 5.5, 3.4, "updateMessage('Escribiendo archivo...')",
          col="#784212", note="updateProgress(3 / 4)")
seq_arrow(ax, 5.5, 10.0, 2.8, "serializar(gymData, backup_<ts>.dat)")
seq_arrow(ax, 10.0, 5.5, 2.3, "archivo escrito", ret=True)
seq_arrow(ax, 5.5, 5.5, 1.8, "updateProgress(4 / 4)",
          col="#784212", note="return filename")
seq_arrow(ax, 5.5, 1.8, 1.3, "setOnSucceeded -> AlertUtil.info(...)",
          ret=True, col="#1E8449",
          note="ejecutado en hilo JavaFX automaticamente")

plt.tight_layout(pad=0.5)
plt.savefig(f"{OUT}/diagrama_secuencia_backup.png", dpi=150,
            bbox_inches="tight", facecolor=BG)
plt.close()
print("6. diagrama_secuencia_backup.png")

print("\nTodos los diagramas generados en:", OUT)
