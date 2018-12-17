package by.it.naumenko.Calculator;

public class Matrix extends Var {
    private double[][] value;

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("{{");
        for (int i = 0; i < value.length; i++) {
            String del = "";
            for (int j = 0; j < value[i].length; j++) {
                stringBuilder.append(del).append(value[i][j]);
                del = ", ";
            }
            if (i != value.length - 1) {
                del = "}, {";
                stringBuilder.append(del);
            } else
                stringBuilder.append("}}");
        }

        return stringBuilder.toString();
    }

    public Matrix(double[][] value) {
        this.value = value;
    }

    Matrix(Matrix matrix) {
        value = matrix.value;
    }

    Matrix(String strMatrix) {
        strMatrix = strMatrix.replaceAll("[},{]", " ");
        strMatrix = strMatrix.trim();
        String[] mas = strMatrix.split("\\s{3,}");
        String[] masCol = mas[0].split("\\s+");//узнать количество столбцов
        int rowCount = mas.length;//количество строк
        int colCount = masCol.length;//количество столбцов

        double massiv[][] = new double[rowCount][colCount];//массим с элементами
        for (int i = 0; i < mas.length; i++) {
            String massivel[] = mas[i].split("\\s+");//разделяем элементы столбца каждой строки
            for (int j = 0; j < massivel.length; j++) {
                massiv[i][j] = Double.parseDouble(massivel[j]);//заполняем массив
            }
        }
        this.value = massiv;
    }

    @Override
    public Var add(Var other) throws CalcExeption {
        //Сумма матрицы  и вектора
        if (other instanceof Vector)
            throw new CalcExeption("Невозможно сложить матрицу и вектор");
        //Сумма матрицы  из матрицы
        if (other instanceof Matrix) {
            Matrix operand2 = (Matrix) other;
            if((this.value.length!=operand2.value.length)||(this.value[0].length!=operand2.value[0].length))
                throw new CalcExeption("Не одинаковы размер матриц");
            double[][] massiv = new double[this.value.length][this.value.length];
            for (int i = 0; i < this.value.length; i++) {
                for (int j = 0; j < this.value[i].length; j++) {
                    massiv[i][j] = this.value[i][j] + operand2.value[i][j];
                }
            }
            return new Matrix(massiv);
        }
        //Сумма матрицы  и числа
        if (other instanceof Scalar) {
            Scalar operand2 = (Scalar) other;
            double[][] massiv = new double[this.value.length][this.value[0].length];
            for (int i = 0; i < this.value.length; i++) {
                for (int j = 0; j < this.value[i].length; j++) {
                    massiv[i][j] = this.value[i][j] + operand2.value;
                }
            }
            return new Matrix(massiv);
        }
        return other.add(this);
    }

    public Var sub(Var other) throws CalcExeption {
        if (other instanceof Vector)
            throw new CalcExeption("Невозможно отнять вектор от матрицы");
        //Вычетание матрицы  из матрицы
        if (other instanceof Matrix) {
            Matrix operand2 = (Matrix) other;
            if((this.value.length!=operand2.value.length)||(this.value[0].length!=operand2.value[0].length))
                throw new CalcExeption("Не одинаковы размер матриц");
            double[][] massiv = new double[this.value.length][this.value.length];
            for (int i = 0; i < this.value.length; i++) {
                for (int j = 0; j < this.value[i].length; j++) {
                    massiv[i][j] = this.value[i][j] - operand2.value[i][j];
                }
            }
            return new Matrix(massiv);
        }
        //Разность матрицы и числа
        if (other instanceof Scalar) {
            Scalar operand2 = (Scalar) other;
            double[][] massiv = new double[this.value.length][this.value[0].length];
            for (int i = 0; i < this.value.length; i++) {
                for (int j = 0; j < this.value[i].length; j++) {
                    massiv[i][j] = this.value[i][j] - operand2.value;
                }
            }
            return new Matrix(massiv);
        }
        return other.sub(this).mul(new Scalar(-1));
    }

    @Override
    public Var mul(Var other) throws CalcExeption {
        //произведение матрицы на число
        if (other instanceof Scalar) {
            Scalar operand2 = (Scalar) other;
            double[][] matr = new double[this.value.length][this.value[0].length];
            for (int i = 0; i < this.value.length; i++) {
                for (int j = 0; j < this.value[i].length; j++) {
                    matr[i][j] = this.value[i][j] * operand2.value;
                }
            }
            return new Matrix(matr);
        }
        //произведение матрицы на матрицу
        if (other instanceof Matrix) {
            Matrix operand2 = (Matrix) other;
            if(this.value[0].length!=operand2.value.length)
                throw new CalcExeption("Матрицы несовместимы");
            double[][] matr = new double[this.value.length][operand2.value[0].length];
            for (int i = 0; i < this.value.length; i++) {
                for (int j = 0; j < operand2.value[0].length; j++) {
                    for (int k = 0; k < operand2.value.length; k++) {
                        matr[i][j] += this.value[i][k] * operand2.value[k][j];
                    }
                }
            }
            return new Matrix(matr);
        }

        if (other instanceof Vector) {
            Vector operand2 = (Vector) other;
            if(this.value[0].length!=operand2.value.length)
                throw new CalcExeption("Матрица и вектор не совместимы");
            double[] vector = new double[this.value.length];
            for (int i = 0; i < this.value.length; i++) {
                for (int j = 0; j < operand2.value.length; j++) {
                    vector[i] += this.value[i][j] * operand2.value[j];
                }
            }
            return new Vector(vector);
        }
        throw new CalcExeption("невозможно умножить вектор на матрицу");
    }


    public Var div(Var other) throws CalcExeption {
        //частное матрицы на число
        if (other instanceof Scalar) {
            Scalar operand2 = (Scalar) other;
            double[][] matr = new double[this.value.length][this.value[0].length];
            for (int i = 0; i < this.value.length; i++) {
                for (int j = 0; j < this.value[i].length; j++) {
                    matr[i][j] = this.value[i][j] / operand2.value;
                }
            }
            return new Matrix(matr);
        }
        //Частное матрицы на матрицу
        if (other instanceof Matrix) {
            throw new CalcExeption("Матрицу нельзя делит на матрицу");
        }

        if (other instanceof Vector) {
            throw new CalcExeption("Матрицу нельзя делить на вектор");
        }
        throw new CalcExeption("делить число на матрицу");
    }
}