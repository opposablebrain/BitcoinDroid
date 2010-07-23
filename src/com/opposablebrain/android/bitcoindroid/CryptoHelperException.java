/* $Id: CryptoHelperException.java 31 2008-10-31 23:56:29Z osborn.steven $
 * 
 * Copyright 2007-2008 Steven Osborn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.opposablebrain.android.bitcoindroid;

/**
 * @author Steven Osborn - http://steven.bitsetters.com
 * 
 * Modified for reuse.
 * @author Rodin Lyasoff - http://opposablebrain.com
 */
public class CryptoHelperException extends Exception {

    private static final long serialVersionUID = 9198874648607918125L;

    public CryptoHelperException(String message) {
		super(message);
    }

}