import os
import re

INSTRUCTIONS_PATH = ".github/copilot-instructions.md"
JAVA_SRC_ROOT = "src/main/java"

def get_entities():
    entities = []
    entity_dir = os.path.join(JAVA_SRC_ROOT, "entity")
    if os.path.exists(entity_dir):
        for f in os.listdir(entity_dir):
            if f.endswith(".java"):
                entities.append(f.replace(".java", ""))
    return sorted(entities)

def get_controllers():
    controllers = {}
    controller_dir = os.path.join(JAVA_SRC_ROOT, "controller")
    if os.path.exists(controller_dir):
        for f in os.listdir(controller_dir):
            if f.endswith(".java"):
                name = f.replace(".java", "")
                path = os.path.join(controller_dir, f)
                with open(path, 'r') as file:
                    content = file.read()
                    # Basic regex for @RequestMapping or @GetMapping at class level (though usually method level here)
                    # Let's just list the controllers and their primary purpose from their names
                    controllers[name] = []
                    # Find @GetMapping, @PostMapping, etc.
                    # Handle @RequestMapping at class level
                    class_mapping = re.search(r'@RequestMapping\((?:value\s*=\s*)?"([^"]+)"\)', content)
                    base_path = class_mapping.group(1) if class_mapping else ""
                    
                    # Improved regex to handle (value = "/path") or ("/path") and multiple attributes
                    matches = re.findall(r'@(Get|Post|Put|Delete)Mapping\((?:[^)]*value\s*=\s*)?"([^"]+)"[^)]*\)', content)
                    # Also catch bare mappings like @GetMapping
                    bare_matches = re.findall(r'@(Get|Post|Put|Delete)Mapping(?!\()', content)
                    
                    for m in matches:
                        verb = m[0]
                        sub_path = m[1]
                        full_path = (base_path + sub_path).replace("//", "/")
                        if not full_path: full_path = "/"
                        controllers[name].append(f"{verb.upper()} {full_path}")
                    
                    for verb in bare_matches:
                        full_path = base_path if base_path else "/"
                        controllers[name].append(f"{verb.upper()} {full_path}")
    return controllers

def update_instructions():
    if not os.path.exists(INSTRUCTIONS_PATH):
        print(f"Error: {INSTRUCTIONS_PATH} not found.")
        return

    with open(INSTRUCTIONS_PATH, 'r') as f:
        lines = f.readlines()

    new_lines = []
    skip = False
    
    # Sections to auto-update
    # 1. Data Models & Relationships (Entities)
    # 2. Package Structure (if changed, but it's static)
    # 3. REST Endpoint Patterns (from controllers)

    i = 0
    while i < len(lines):
        line = lines[i]
        
        # Update Entities list
        if "### Data Models & Relationships" in line:
            new_lines.append(line)
            entities = get_entities()
            for entity in entities:
                # Keep existing descriptions if possible? 
                # For now, let's just ensure they are all listed.
                # Simple logic: If the next lines are list items, skip them and replace with our new list.
                exists = False
                for j in range(i+1, min(i+20, len(lines))):
                    if f"- **{entity}**" in lines[j]:
                        exists = True
                        break
                if not exists:
                    new_lines.append(f"- **{entity}**: [Auto-detected entity]\n")
            
            # Skip old entity lines if we want to fully manage it, but user might have hand-written notes.
            # Let's just append new ones for now.
            i += 1
            continue

        # Update REST Endpoints
        if "### REST Endpoint Patterns" in line:
            new_lines.append(line)
            controllers = get_controllers()
            # This section is more complex to auto-update without destroying hand-written docs.
            # Maybe add a "### Auto-Generated Endpoints" section?
            i += 1
            continue

        new_lines.append(line)
        i += 1

    with open(INSTRUCTIONS_PATH, 'w') as f:
        f.writelines(new_lines)
    print("Updated copilot-instructions.md")

if __name__ == "__main__":
    # For now, let's do a more structured replacement.
    entities = get_entities()
    entity_str = "\n".join([f"- **{e}**" for e in entities])
    
    controllers = get_controllers()
    endpoint_list = []
    for c, endpoints in controllers.items():
        for e in endpoints:
            endpoint_list.append(f"- `{e}`")
    endpoint_str = "\n".join(endpoint_list)

    with open(INSTRUCTIONS_PATH, 'r') as f:
        content = f.read()

    # We will use markers in the file to identify where to inject
    # If markers don't exist, we should probably add them or use headers.
    
    # Let's try to replace the content under headers.
    
    # 1. Update Entities
    entity_section_pattern = r"(### Data Models & Relationships\n)([\s\S]*?)(---|\n##)"
    def entity_repl(match):
        header = match.group(1)
        # Preserve lines starting with - ** but ensure all entities are there?
        # Simpler: just replace the block with a list of all found entities.
        # But we lose hand-written descriptions.
        # Let's try to merge.
        existing = match.group(2)
        new_block = header
        for e in entities:
            if f"**{e}**" in existing:
                # find the line and keep it
                line = re.search(rf"- \*\*{e}\*\*.*", existing)
                if line:
                    new_block += line.group(0) + "\n"
            else:
                new_block += f"- **{e}**: Professional sports entity\n"
        # Add the remaining lines that were there but not identified as entities (like Serialization)
        for line in existing.splitlines():
            if line.strip() and not any(f"**{e}**" in line for e in entities):
                new_block += line + "\n"
        return new_block + match.group(3)

    content = re.sub(entity_section_pattern, entity_repl, content)

    # 2. Update Endpoints
    endpoint_section_pattern = r"(### REST Endpoint Patterns\n)([\s\S]*?)(---|\n##)"
    def endpoint_repl(match):
        header = match.group(1)
        existing = match.group(2)
        new_block = header
        found_endpoints = []
        for c, endpoints in controllers.items():
            for e in endpoints:
                found_endpoints.append(e)
        
        # Sort found endpoints for consistent order
        found_endpoints.sort()

        # Keep track of what we added to avoid duplicates
        added = set()

        # ONLY add endpoints that actually exist in the code
        for e in found_endpoints:
            if e in added: continue
            # Check if this endpoint already exists in 'existing' (possibly with query params or custom desc)
            # e is "VERB PATH"
            verb, path = e.split(' ', 1)
            pattern = rf"- `({verb} )?{re.escape(path)}(\?[^`]+)?`"
            if re.search(pattern, existing):
                line = re.search(rf"- `({verb} )?{re.escape(path)}(\?[^`]+)?`.*", existing)
                if line:
                    new_block += line.group(0) + "\n"
            else:
                new_block += f"- `{e}`: API Endpoint\n"
            added.add(e)
        
        # Keep non-endpoint lines (like descriptions) that don't match the auto-format
        # AND are NOT list items for endpoints that no longer exist
        # SPECIAL CASE: Keep manually enhanced descriptions for health and images
        for line in existing.splitlines():
            line_content = line.strip()
            if not line_content: continue
            
            # Check if this line is an endpoint list item
            endpoint_match = re.search(r"- `([A-Z]+ )?([^` ?]+)(\?[^`]+)?`", line_content)
            if endpoint_match:
                # It's an endpoint line. Check if it's in our currently found endpoints.
                current_ep_path = endpoint_match.group(2)
                # Note: found_endpoints contains "VERB PATH", so we need to be careful
                if any(ep.endswith(current_ep_path) for ep in found_endpoints):
                    # It's a valid current endpoint.
                    # If it has a custom description (not "API Endpoint"), keep the WHOLE line
                    # OR if it has query parameters, keep it (as we don't auto-detect them yet)
                    if "API Endpoint" not in line or "?" in line_content:
                         # We already added an auto-generated one for this same path in the first loop.
                         # To avoid duplicates, we should have a better strategy.
                         # For now, let's just make sure we don't add it in the first loop if we are going to keep it here.
                         pass
                    continue
                else:
                    # It's an old endpoint that no longer exists. Skip it.
                    continue
            
            # If it's not an endpoint item, keep it
            new_block += line + "\n"
        return new_block + match.group(3)

    content = re.sub(endpoint_section_pattern, endpoint_repl, content)

    with open(INSTRUCTIONS_PATH, 'w') as f:
        f.write(content)
    print("Instructions updated successfully.")
