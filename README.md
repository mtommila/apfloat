# Apfloat

Copyright © 2021 Mikko Tommila

This work is licensed under the terms of the MIT license. See the [MIT License](LICENSE.md) for more details.

If you have any questions or need a different type of license, please [contact the author](mailto:Mikko.Tommila@apfloat.org).

## Building the Library

To build the signed applet files, you need to first generate a signing key, e.g. with:

`keytool -genkeypair -validity 21915 -dname "cn=Your Name, o=example.com" -storepass password -keypass password -alias mykey`

To build the library quickly, without running unit tests (takes about 10 minutes) and without signing with GPG run:

`mvn clean install -Dgpg.skip=true -DskipTests=true`

## Running the Sample Applications

To run the arbitrary precision calculator, run:

`mvn -pl :apfloat-calc exec:java -Dexec.mainClass=org.apfloat.calc.CalculatorGUI`

To run the pi calculator, run:

`mvn -pl :apfloat-samples exec:java -Dexec.mainClass=org.apfloat.samples.PiParallelGUI`

## Apfloat website

Go to the [apfloat for Java website](http://www.apfloat.org/apfloat_java/).
