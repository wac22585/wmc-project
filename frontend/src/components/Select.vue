<template>
  <select id="mySelect" v-model="selectedRoles" class="select" name="Roles">
    <option value="" disabled>Select a role</option>
    <option v-for="role in roles" :key="role" :value="role">{{ role }}</option>
  </select>
</template>

<script>
export default {
  props: {
    roles: Array,
  },
  data() {
    return {
      selectedRoles: null,
    };
  },
};
</script>

<style>
  .select {
    border: #707070 1px solid;
    background: white !important;
    height: 40px;
    width: 400px;
    margin: 10px 0px;
    border-radius: 7px;
    padding-inline: 10px;
    transition: border-color 0.3s;
    display: block;
  }
</style>
