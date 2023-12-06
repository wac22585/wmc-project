<template>
  <h1>Hello World</h1>
</template>

<script setup> 
  import axios from 'axios';
</script>

<script>
  export default {
      data() {
          return {
              users: [],
          };
      },
      async mounted() {
          try {
              const response = await axios.get("users/all");
              this.users = response.date;
          }
          catch(e)
          {
              alert(e)
          }
      },
      methods: {

      },
  }
</script>