## File Format

The format of training and testing SVM data file is:
```
<label> <index1>:<value1> <index2>:<value2> ...
.
.
.
```
Each line contains an instance and is ended by a '\n' character.  For classification, `<label>` is an integer indicating the class label. The `<index>` value starts from 1 and the `<value>` can be any real number indicating feature values.
