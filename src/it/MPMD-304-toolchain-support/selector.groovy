/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

def nodename = System.getenv( "NODE_NAME" )

File userToolchains = new File( System.getProperty( 'user.home' ), '.m2/toolchains.xml' )
if ( userToolchains.exists() )
{
    System.out.println( "INFO: Found ${userToolchains.absolutePath}" )
    System.out.println( userToolchains.text )

    def toolchains = new XmlParser().parseText( userToolchains.text )
    def result = toolchains.children().find { toolchain ->
            toolchain.type.text() == 'jdk' &&
            toolchain.provides.version.text() == '11' &&
            toolchain.provides.vendor.text() == 'oracle'
    }
    if ( !result )
    {
        System.out.println( "WARNING: No jdk toolchain for 11:oracle found" )
        throw new RuntimeException( "WARNING: No jdk toolchain for 11:oracle found in ${userToolchains.absolutePath} on node ${nodename}:\n${userToolchains.text}" )
        return false
    }

    System.out.println( "INFO: Found toolchain: ${result}" )
    return true
}

System.out.println( "WARNING: Skipping integration test due to missing toolchain file in ${userToolchains.absolutePath}" )
throw new RuntimeException( "WARNING: Skipping integration test due to missing toolchain file in ${userToolchains.absolutePath} on node ${nodename}" )
return false
