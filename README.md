# Apfloat

Copyright © 2017 Mikko Tommila

This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation; either version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the [GNU Lesser General Public License](LICENSE.md) for more details.

If you have any questions or need a different type of license, please [contact the author](mailto:Mikko.Tommila@apfloat.org).

## Building the Library

To build the signed applet files, you need to first generate a signing key, e.g. with:

`keytool -genkeypair -validity 21915 -dname "cn=Your Name, o=example.com" -storepass password -keypass password -alias mykey`

To build the library quickly, without running unit tests (takes about 10 minutes) and without signing with GPG run:

`mvn clean install -DskipTests=true -Dgpg.skip=true`

## Running the Sample Applications

To run the arbitrary precision calculator, run:

`mvn -pl :apfloat-calc exec:java -Dexec.mainClass=org.apfloat.calc.CalculatorGUI`

To run the pi calculator, run:

`mvn -pl :apfloat-samples exec:java -Dexec.mainClass=org.apfloat.samples.PiParallelGUI`
