<template>
    <div class="input-container">
      <div class="input-with-icon">
        <input :type="inputType === 'secure' || (inputType === 'password' && !showPassword) ? 'password' : 'text'" 
        class="input-form"
        :value="modelValue"
        @input="$emit('update:modelValue', $event.target.value)"
        :style="{width: width}"
        :placeholder="label"
        :readonly="readonly"
        >
        <div v-if="search" class="search-icon">
          <v-icon>mdi-magnify</v-icon>
        </div>
        <div v-if="inputType === 'password'" class="eye-icon" @click="togglePasswordVisibility">
          <v-icon>
            {{ showPassword ? 'mdi-eye' : 'mdi-eye-off' }}
          </v-icon>
        </div>
      </div>
    </div>
  </template>

  <script setup>
  import { computed } from 'vue'

  const props = defineProps(['modelValue', 'inputType', 'width', 'label', 'search', 'readonly'])
  const emit = defineEmits(['update:modelValue'])

  const value = computed({
    get() {
      return props.modelValue
    },
    set(value) {
      emit('update:modelValue', value)
    }
  })
  </script>
  
  <script>
  export default {
    data() {
      return {
        text: '',
        showPassword: false,
        email: '',
        password: '',
      };
    },
    methods: {
      onFocus() {
        this.$el.style.borderColor = '#000';
      },
      onBlur() {
        this.$el.style.borderColor = '#707070';
      },
      togglePasswordVisibility() {
        this.showPassword = !this.showPassword;
      }
    }
  };
  </script>
  
  <style>
  .input-container {
    position: relative;
  }
  
  .input-with-icon {
    position: relative;
  }
  
  .input-form {
    border: #707070 1px solid;
    height: 50px;
    margin: 10px 0px;
    width: 100%;
    min-width: 300px;
    border-radius: 7px;
    padding-inline: 10px;
    transition: border-color 0.3s;
  }
  
  .input-form:focus {
    outline: none;
  }
  
  .eye-icon, .search-icon {
    position: absolute;
    top: 50%;
    transform: translateY(-50%);
    cursor: pointer;
  }

  .eye-icon {
    right: 10px;
  }

  .search-icon {
    left: 10px;
  }
  </style>
  