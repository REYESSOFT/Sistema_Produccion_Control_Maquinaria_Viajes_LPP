import sys
from PySide6.QtWidgets import QApplication
from login import LoginWindow


def main():
    app = QApplication(sys.argv)

    app.setApplicationName("LPP Smart ERP")
    app.setOrganizationName("Grupo LPP")

    ventana = LoginWindow()
    ventana.show()

    sys.exit(app.exec())


if __name__ == "__main__":
    main()