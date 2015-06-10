package com.ghostbuster.warsawApi.utils

import com.fasterxml.jackson.databind.PropertyNamingStrategy

final class UpperCaseWithUnderscoresStrategy extends PropertyNamingStrategy.PropertyNamingStrategyBase
{
    @Override
    public String translate(String input) {
        if (input == null) return input // garbage in, garbage out

        return input.toUpperCase()
    }
}
