import tkinter as tk


class Application(tk.Tk):
    def __init__(self):
        super().__init__()
        self.title("Moja Aplikacja")

        self.label = tk.Label(self, text="Witaj w mojej aplikacji!", font=("Helvetica", 16))
        self.label.pack(pady=10)

        self.button = tk.Button(self, text="Kliknij mnie", command=self.on_button_click)
        self.button.pack(pady=5)

    def on_button_click(self):
        self.label.config(text="Kliknięto przycisk!")


if __name__ == "__main__":
    app = Application()
    app.mainloop()
